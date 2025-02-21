package com.server.delivery.domain.menu.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomMenuException;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.common.exception.customException.CustomUserException;
import com.server.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.server.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.menu.repository.MenuRepository;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;
import com.server.delivery.util.s3image.S3ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final S3ImageUtil s3ImageUtil;
    private final UserHelper userHelper;

    @Transactional
    public void registerMenu(
            Long userId,
            UUID storeUuid,
            MenuCreateRequestDto requestDto,
            MultipartFile foodImage) {
        userHelper.getUserById(userId);

        Store store = getStore(storeUuid);
        validateIsUsersStore(userId, store);
        validateIsMenuExist(store, requestDto.getFoodName());

        String uploadedImageToS3 = s3ImageUtil.uploadImageToS3(foodImage);

        Menu menu = MenuCreateRequestDto.to(requestDto, store, uploadedImageToS3);

        menuRepository.save(menu);

    }

    @Transactional
    public void updateMenu(
            Long userId, UUID menuUuid,
            MenuUpdateRequestDto requestDto,
            MultipartFile foodImage) {

        Menu menu = getMenuByMenuUuid(menuUuid);
        validateIsUsersStore(userId, menu.getStore());
        validateIsMenuExist(menu, requestDto.getFoodName());

        if (!menu.getFoodImage().isEmpty()) {
            s3ImageUtil.deleteImageFromS3(menu.getFoodImage());
        }
        String uploadedImageToS3 = s3ImageUtil.uploadImageToS3(foodImage);

        menu.setMenuName(requestDto.getFoodName());
        menu.setMenuAvailability(requestDto.isAvailability());
        menu.setMenuPrice(requestDto.getPrice());
        menu.setMenuDescription(requestDto.getDescription());
        menu.setFoodImage(uploadedImageToS3);

        menuRepository.save(menu);

    }


    @Transactional
    public void deleteMenu(
            Long userId, UUID menuUuid) {
        Menu menu = getMenuByMenuUuid(menuUuid);
        validateIsUsersStore(userId, menu.getStore());
        menu.setMenuAvailability(false);
        menu.softDelete();
        menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public MenuResponseDto getMenu(
            Long userId, UUID menuUuid) {
        Menu menu = getMenuByMenuUuid(menuUuid);

        validateIsUsersStore(userId, menu.getStore());

        return MenuResponseDto.from(menu);
    }


    public PageCustom<MenuResponseDto> searchMenus(
            Long userId,
            UUID storeUuid, String keyword,
            Pageable pageable
    ) {
        userHelper.getUserById(userId);
        Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("modifiedAt"));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);

        Store store = getStore(storeUuid);

        Page<Menu> menuList = menuRepository.findByStoreAndMenuNameContainingAndMenuAvailabilityTrue(store, keyword, pageable);

        List<MenuResponseDto> userDtoList = menuList.getContent().stream()
                .map(MenuResponseDto::from) // UserResponseDto 변환 메서드 필요
                .toList();

        return new PageCustom<>(userDtoList, sortedPageable, menuList.getTotalElements());

    }

    private Menu getMenuByMenuUuid(UUID menuUuid) {
        return menuRepository.findByMenuUuId(menuUuid).orElseThrow(
                () -> new CustomMenuException(ExceptionCode.MENU_NOT_FOUND)
        );
    }

    private Store getStore(UUID storeUuid) {
        return storeRepository.findByStoreUuid(storeUuid).orElseThrow(
                () -> new CustomStoreException(ExceptionCode.STORE_NOT_FOUND)
        );
    }

    private void validateIsUsersStore(Long userId, Store store) {
        User user = userHelper.getUserById(userId);
        if (!user.getUserStores().isEmpty()) {
            boolean isUsersStore = user.getUserStores().stream().anyMatch(
                    userStore -> userStore.getStore().equals(store)
            );
            if (!isUsersStore) {
                throw new CustomUserException(ExceptionCode.STORE_NOT_MATCH);
            }
        }
    }

    private void validateIsMenuExist(Store store, String foodName) {
        boolean isMenuExist = store.getMenus().stream().anyMatch(
                menu -> menu.getMenuName().equals(foodName)
        );
        if (isMenuExist) {
            throw new CustomMenuException(ExceptionCode.MENU_IS_EXIST);
        }
    }

    private void validateIsMenuExist(Menu menu, String foodName) {
        if (menu.getMenuName().equals(foodName)) {
            throw new CustomMenuException(ExceptionCode.MENU_IS_EXIST);
        }

    }
}
