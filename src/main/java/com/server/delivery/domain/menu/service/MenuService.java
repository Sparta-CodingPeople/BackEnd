package com.server.delivery.domain.menu.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.server.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MenuService {

    void registerMenu(
            Long userId,
            UUID storeUuid,
            MenuCreateRequestDto requestDto,
            MultipartFile foodImage);

    void updateMenu(
            Long userId, UUID menuUuid,
            MenuUpdateRequestDto requestDto,
            MultipartFile foodImage);

    void deleteMenu(
            Long userId,
            UUID menuUuid
    );

    MenuResponseDto getMenu(
            Long userId,
            UUID menuUuid
    );

    PageCustom<MenuResponseDto> searchMenus(
            Long userId,
            UUID storeUuid,
            String keyword,
            Pageable pageable
    );


}
