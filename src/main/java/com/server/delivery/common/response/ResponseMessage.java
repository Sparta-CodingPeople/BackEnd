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
	REVIEW_DELETE("리뷰 삭제 성공");

	private final String message;
}
