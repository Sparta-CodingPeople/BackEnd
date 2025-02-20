package com.server.delivery.domain.store.dto.request;

import lombok.Getter;

@Getter
public class StoreUpdateRequestDto {
    private String storeName;
    private int storeCategoryId;
    private String phoneNumber;
    private String storeDescription;
}
