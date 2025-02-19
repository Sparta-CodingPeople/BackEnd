package com.server.delivery.domain.review.dto.res;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetailSearchResponseDto(
	Double rating,
	String content,
	List<String> images,
	LocalDateTime createdAt,
	String createdBy
) {
}
