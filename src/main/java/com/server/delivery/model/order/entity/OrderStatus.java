package com.server.delivery.model.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    WAITING(1, "주문 승인 대기중"),
    DELIVERING(2, "배달중"),
    REJECTED(3, "주문 거절"),
    ACCEPT(4, "주문 승인"),
    CANCELED(5, "주문 취소"),
    COMPLETE(6, "배달 완료됨");

    private final int number;
    private final String koreanValue;

    // 숫자를 enum 값으로 변환하는 메소드
    public static OrderStatus fromNumber(int number) {
        for (OrderStatus orderStatus : values()) {
            if (orderStatus.getNumber() == number) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid number: " + number);
    }


}
