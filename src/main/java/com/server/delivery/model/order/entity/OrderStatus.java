package com.server.delivery.model.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
	WAITING,
	DELIVERING,
	REJECTED,
	ACCEPT,
	CANCELED
}
