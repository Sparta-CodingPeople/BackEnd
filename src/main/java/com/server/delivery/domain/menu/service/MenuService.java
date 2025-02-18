package com.server.delivery.domain.menu.service;

import com.server.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.server.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    public int registerMenu(
            UUID restaurantId,
            MenuCreateRequestDto requestDto,
            MultipartFile foodImage) {
        return 0;
    }

    public int updateMenu(
            UUID restaurantId,
            UUID menuId,
            MenuUpdateRequestDto requestDto) {
        return 0;
    }

    public int deleteMenu(
            UUID restaurantId,
            UUID menuId) {
        return 0;
    }

    public MenuResponseDto getMenu(
            UUID restaurantId,
            UUID menuId) {
        return null;
    }

    public List<MenuResponseDto> searchMenus(
            UUID restaurantId,
            String search,
            Pageable pageable) {
        return null;
    }

    public Long getTotalMenus(
            UUID restaurantId,
            String search) {
        return null;
    }
}
