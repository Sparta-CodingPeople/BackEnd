package com.server.delivery.domain.store.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreRegisterRequestDto {
    private StoreInfoDto storeInfo;
    private StoreLocationRequestDto storeLocation;
    private List<StoreOperatingHoursRequestDto> operatingHours;
}
