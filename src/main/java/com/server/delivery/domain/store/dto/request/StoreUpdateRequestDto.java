package com.server.delivery.domain.store.dto.request;

import lombok.Getter;

@Getter
public class StoreUpdateRequestDto {
    private String storeName;
    private String storeCategory;
    private String phoneNumeber;
    private String storeDescription;
}
