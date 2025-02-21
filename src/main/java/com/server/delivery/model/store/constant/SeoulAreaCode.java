package com.server.delivery.model.store.constant;

import lombok.Getter;

@Getter
public enum SeoulAreaCode {

    GWANGHWAMOON(1);

    private final int number;

    SeoulAreaCode(int number) {
        this.number = number;
    }

    // 숫자를 enum 값으로 변환하는 메소드
    public static SeoulAreaCode fromNumber(int number) {
        for (SeoulAreaCode areaCode : values()) {
            if (areaCode.getNumber() == number) {
                return areaCode;
            }
        }
        throw new IllegalArgumentException("Invalid number: " + number);
    }

}
