package com.server.delivery.common.pagination;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PageSize {
	DEFAULT(10),
	MEDIUM(30),
	LARGE(50);

	private final int size;

	public static int of(int requestedSize) {
		return Arrays.stream(values())
			.map(PageSize::getSize)
			.filter(size -> size == requestedSize)
			.findFirst()
			.orElse(DEFAULT.size);
	}
}
