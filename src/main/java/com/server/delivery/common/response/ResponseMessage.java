package com.server.delivery.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
	// 리뷰
	REVIEW_CREATED("리뷰 생성 성공"),
	REVIEW_UPDATED("리뷰 수정 성공"),
	REVIEW_SEARCH("리뷰 단일 조회 성공"),
	STORE_REVIEW_SEARCH("음식점 리뷰 목록 조회 성공"),
	USER_REVIEW_SEARCH("사용자 리뷰 목록 조회 성공"),
	REVIEW_DELETE("리뷰 삭제 성공"),

	// 결제
	PAYMENT_TEMP_SAVE_AMOUNT("결제 금액 임시 저장 성공"),
	PAYMENT_VERIFY_AMOUNT("결제 금액 검증 성공"),
	PAYMENT_REQUEST("결제 요청 성공"),
	PAYMENT_CANCEL("결제 취소 성공"),
	PAYMENT_SEARCH("결제 단일 내역 조회 성공"),
	PAYMENT_SEARCH_ALL("결제 내역 목록 조회 성공");

	private final String message;
}
