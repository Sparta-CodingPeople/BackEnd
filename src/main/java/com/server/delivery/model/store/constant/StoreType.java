package com.server.delivery.model.store.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreType {
    KOREAN_FOOD(1, "한식"),
    CHINESE_FOOD(2, "중식"),
    WESTERN_FOOD(3, "양식"),
    JAPANESE_FOOD(4, "일식"),
    INDIAN_FOOD(5, "인도식"),
    FAST_FOOD(6, "패스트푸드"),
    DESSERT(7, "디저트"),
    CAFE(8, "카페");

    private final int categoryId;
    private final String categoryNameKorean;


    // 숫자를 enum 값으로 변환하는 메소드
    public static StoreType fromNumber(int number) {
        for (StoreType category : values()) {
            if (category.getCategoryId() == number) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category number: " + number);
    }
}