package com.server.delivery.domain.owner.service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.common.exception.customException.CustomUserException;
import com.server.delivery.domain.owner.dto.request.UpdateManagerRequestDto;
import com.server.delivery.model.manager.entity.Manager;
import com.server.delivery.model.manager.repository.ManagerRepository;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.StoreHelper;
import com.server.delivery.util.helper.UserHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final UserHelper userHelper;
    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;
    private final StoreHelper storeHelper;


    @Override
    @Transactional
    public void setManager(Long toBeManagerUserId, UpdateManagerRequestDto updateManagerRequestDto, Long ownerUserId) {
        User ownerUser = userHelper.getUserById(ownerUserId);
        User toBeManagerUser = userHelper.getUserById(toBeManagerUserId);

        Store store = storeHelper.getStoreByUuid(updateManagerRequestDto.getStoreUuid());
        if (store.getUserStore() != null && store.getUserStore().stream().noneMatch(
                userStore -> userStore.getUser().equals(ownerUser)
        )) {
            throw new CustomUserException(ExceptionCode.OWNER_IS_NOT_MATCHED);
        }

        if (store.getManager() != null) {
            throw new CustomStoreException(ExceptionCode.MANAGER_ALREADY_EXIST);
        }

        Manager manager = Manager.builder()
                .store(store)
                .user(toBeManagerUser)
                .build();

        Manager savedManager = managerRepository.save(manager);

        store.setManager(savedManager);

    }

    @Override
    @Transactional
    public void updateManager(Long changedUserId, UpdateManagerRequestDto updateManagerRequestDto, Long userId) {
        Store store = storeHelper.getStoreByUuid(updateManagerRequestDto.getStoreUuid());
        User changedUser = userHelper.getUserById(changedUserId);
        Manager oldManager = store.getManager();
        oldManager.setDeleted(true);
        oldManager.softDelete();
        managerRepository.save(oldManager);
        store.setManager(null);

        storeRepository.save(store);

        Manager manager = Manager.builder()
                .store(store)
                .user(changedUser)
                .build();

        Manager savedManager = managerRepository.save(manager);

        store.setManager(savedManager);
    }
}
