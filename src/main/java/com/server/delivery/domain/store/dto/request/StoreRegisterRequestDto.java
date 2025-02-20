package com.server.delivery.domain.store.dto.request;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class StoreRegisterRequestDto {
    private StoreInfoDto storeInfo;
    private StoreLocationRequestDto storeLocation;
    private List<StoreOperatingHoursRequestDto> operatingHours;
    private UUID storeLocationUuid;
}
