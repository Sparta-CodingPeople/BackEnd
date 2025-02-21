package com.server.delivery.model.store.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeekDays {
    MON(1, "월요일"),
    TUE(2, "화요일"),
    WED(3, "수요일"),
    THU(4, "목요일"),
    FRI(5, "금요일"),
    SAT(6, "토요일"),
    SUN(7, "일요일");

    private final int weekday;
    private final String koreanName;

    // 숫자를 enum 값으로 변환하는 메소드
    public static WeekDays fromNumber(int number) {
        for (WeekDays days : values()) {
            if (days.getWeekday() == number) {
                return days;
            }
        }
        throw new IllegalArgumentException("Invalid number: " + number);
    }
}
