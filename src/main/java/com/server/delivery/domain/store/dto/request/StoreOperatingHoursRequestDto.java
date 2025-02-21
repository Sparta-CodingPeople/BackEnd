package com.server.delivery.domain.store.dto.request;

import com.server.delivery.model.store.constant.WeekDays;
import com.server.delivery.model.store.entity.OperationTimes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreOperatingHoursRequestDto {
    private String operationTimeOpeningTime;
    private String operationTimeClosingTime;
    private boolean isHoliday;
    private int weekdays;

    public static OperationTimes from(StoreOperatingHoursRequestDto requestDto) {
        return OperationTimes.builder()
                .operationTimeClosingTime(requestDto.getOperationTimeClosingTime())
                .operationTimeOpeningTime(requestDto.getOperationTimeOpeningTime())
                .isHoliday(requestDto.isHoliday())
                .weekday(WeekDays.fromNumber(requestDto.getWeekdays()))
                .build();

    }
}
