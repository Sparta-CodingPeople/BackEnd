package com.server.delivery.domain.menu.dto.request;

import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.store.entity.Store;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuCreateRequestDto {
    private String foodName;
    private String description;
    private int price;
    private boolean availability;

    public static Menu to(MenuCreateRequestDto requestDto, Store store, String foodImage) {
        return Menu.builder()
                .menuName(requestDto.getFoodName())
                .menuDescription(requestDto.getDescription())
                .menuPrice(requestDto.getPrice())
                .menuAvailability(requestDto.isAvailability())
                .store(store)
                .foodImage(foodImage)
                .build();
    }
}
