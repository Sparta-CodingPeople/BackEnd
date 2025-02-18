package com.server.delivery.domain.menu.dto.response;

import com.server.delivery.common.PageCustom;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
import com.server.delivery.model.menu.entity.Menu;

@Getter
@Builder
public class MenuResponseDto {
    private String status;
    private String message;
    private Object data;

    private UUID menuId;
    private String foodName;
    private String description;
    private int price;
    private boolean availability;
    private String foodImage;

    public static MenuResponseDto from(Menu menu) {
        return MenuResponseDto.builder()
                .menuId(menu.getMenuUuId())
                .foodName(menu.getMenuName())
                .description(menu.getMenuDescription())
                .price(menu.getMenuPrice())
                .availability(menu.isMenuAvailability())
                .foodImage(menu.getFoodImage())
                .build();
    }

    public static MenuResponseDto fromPageCustom(PageCustom<MenuResponseDto> pageCustom) {
        return MenuResponseDto.builder()
                .status("200")
                .message("음식 검색 결과")
                .data(pageCustom)
                .build();
    }
}
