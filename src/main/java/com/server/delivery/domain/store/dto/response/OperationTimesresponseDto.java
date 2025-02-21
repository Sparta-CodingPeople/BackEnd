package com.server.delivery.domain.store.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationTimesresponseDto {
    private String days;
    private String openTime;
    private String closeTime;
    private boolean isHoliday;
}
