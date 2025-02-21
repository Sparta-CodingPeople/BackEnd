package com.server.delivery.model.delivery.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryTip {
	BASIC(3000);

	private final int price;
}
