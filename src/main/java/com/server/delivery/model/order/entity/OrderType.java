package com.server.delivery.model.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    ONLINE(1, "온라인"),
    OFFLINE(2, "오프라인");

    private final int number;
    private final String koreanValue;


    // 숫자를 enum 값으로 변환하는 메소드
    public static OrderType fromNumber(int number) {
        for (OrderType orderType : values()) {
            if (orderType.getNumber() == number) {
                return orderType;
            }
        }
        throw new IllegalArgumentException("Invalid number: " + number);
    }
}
