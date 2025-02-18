package com.server.delivery.domain.menu.dto.request;

import lombok.Getter;

@Getter
public class MenuUpdateRequestDto {
    private String foodName;
    private String description;
    private int price;
    private boolean availability;
}
