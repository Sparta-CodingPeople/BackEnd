package com.server.delivery.domain.store.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreInfoDto {
    private String storeName;
    private int storeCategoryId;
    private String phoneNumber;
    private String storeDescription;
}

