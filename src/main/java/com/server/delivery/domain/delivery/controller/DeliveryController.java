package com.server.delivery.domain.delivery.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.common.response.ResponseMessage;
import com.server.delivery.domain.delivery.dto.req.DeliveryCompleteRequestDto;
import com.server.delivery.domain.delivery.dto.req.DeliveryStartRequestDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryCompleteResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryStartResponseDto;
import com.server.delivery.domain.delivery.service.DeliveryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {
	private final DeliveryService deliveryService;

	// 배달 시작 (사장님)
	@PatchMapping("/v1/deliveries/{deliveryId}/start")
	public CustomResponse<DeliveryStartResponseDto> startDelivery(
		@PathVariable UUID deliveryId,
		@RequestBody DeliveryStartRequestDto request
	) {
		DeliveryStartResponseDto response = deliveryService.startDelivery(deliveryId, request);
		return CustomResponse.success(ResponseMessage.DELIVERY_START.getMessage(), response);
	}

	// 배달 완료 (라이더)
	@PatchMapping("/v1/deliveries/{deliveryId}/complete")
	public CustomResponse<DeliveryCompleteResponseDto> completeDelivery(
		@PathVariable UUID deliveryId,
		@RequestBody DeliveryCompleteRequestDto request
	) {
		DeliveryCompleteResponseDto response = deliveryService.completeDelivery(deliveryId, request);
		return CustomResponse.success(ResponseMessage.DELIVERY_COMPLETE.getMessage(), response);
	}
}
