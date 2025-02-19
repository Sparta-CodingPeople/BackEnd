package com.server.delivery.domain.store.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreRegisterRequestDto {
    private String storeName;
    private int storeCategoryId;
    private String phoneNumber;
    private String storeDescription;
    private StoreLocationRequestDto storeLocation;
    private List<StoreOperatingHoursRequestDto> operatingHours;
}
