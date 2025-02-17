package com.server.delivery.model.user.entity.constant;

import lombok.Getter;

@Getter
public enum UserGender {
    MALE(1),
    FEMALE(2);

    private final int number;

    UserGender(int number) {
        this.number = number;
    }

    // 숫자를 enum 값으로 변환하는 메소드
    public static UserGender fromNumber(int number) {
        for (UserGender gender : values()) {
            if (gender.getNumber() == number) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid number: " + number);
    }

}