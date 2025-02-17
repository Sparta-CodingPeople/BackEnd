package com.server.delivery.domain.menu.service;

import com.server.delivery.domain.menu.dto.request.MenuRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MenuService {

    public int registerMenu(
            UUID restaurantId,
            MenuRequestDto requestDto) {
        return 0;
    }

    public int updateMenu(
            UUID restaurantId,
            UUID menuId,
            MenuRequestDto requestDto) {
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
