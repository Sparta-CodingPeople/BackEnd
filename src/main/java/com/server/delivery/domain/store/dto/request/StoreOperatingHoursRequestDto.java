package com.server.delivery.domain.store.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreOperatingHoursRequestDto {
    private String openingTime;
    private String closingTime;
    private boolean isClosed;
    private int weekday;
}
