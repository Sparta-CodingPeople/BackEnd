package com.server.delivery.model.delivery.entity;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
	WAITING, // 배달 기사가 픽업하기 위해 대기 중인 상태
	DELIVERING, // 배달 기사가 음식을 고객에게 배달 중인 상태
	COMPLETED, // 배달 기사가 음식을 고객에게 전달한 상태
	CANCELED; // 고객의 사유로 인해 배달이 취소된 상태

	public static boolean isAlreadyStarted(DeliveryStatus status) {
		return status == DELIVERING || status == COMPLETED || status == CANCELED;
	}

	public static boolean isNotDelivering(DeliveryStatus status) {
		return status != DELIVERING;
	}

	public static boolean isCompletedDelivery(DeliveryStatus status) {
		return status == COMPLETED;
	}
}
