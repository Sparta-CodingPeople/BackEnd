package com.server.delivery.domain.store.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreOperatingHoursRequestDto {
    private String operationTimeOpeningTime;
    private String operationTimeClosingTime;
    private boolean storeIsClosed;
    private List<String> weekdays;
}
