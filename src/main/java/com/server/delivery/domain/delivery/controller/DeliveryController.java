package com.server.delivery.domain.delivery.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.delivery.domain.delivery.dto.res.DeliverySearchResponseDto;
import com.server.delivery.domain.delivery.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {
	private final DeliveryService deliveryService;

	// 배달 정보 조회
	@GetMapping("/v1/orders/{orderId}/status")
	public ResponseEntity<DeliverySearchResponseDto> searchDeliveryInfo(@PathVariable UUID orderId) {
		DeliverySearchResponseDto response = deliveryService.searchDeliveryInfo(orderId);
		return ResponseEntity.ok(response);
	}
}
