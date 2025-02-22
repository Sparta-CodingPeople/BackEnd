package com.server.delivery.domain.master.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreRepository;
import com.server.delivery.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional(readOnly = true)
    public PageCustom<StoreResponseDto> getStoreListNeedGrant(Long id, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("modifiedAt"));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);

        Page<Store> storePage = storeRepository.findByStoreIsGrantedFalse(sortedPageable);

        // User 엔티티 → UserResponseDto 변환
        List<StoreResponseDto> storeResponseDtoList = storePage.getContent().stream()
                .map(StoreResponseDto::from) // UserResponseDto 변환 메서드 필요
                .toList();

        return new PageCustom<>(storeResponseDtoList, sortedPageable, storePage.getTotalElements());
    }

    @Override
    @Transactional
    public void approveStore(Long userId, UUID storeUuid) {
        Store store = storeRepository.findByStoreUuid(storeUuid).orElseThrow(
                () -> new CustomStoreException(ExceptionCode.STORE_NOT_FOUND)
        );
        store.setStoreIsGranted(true);
    }
}
