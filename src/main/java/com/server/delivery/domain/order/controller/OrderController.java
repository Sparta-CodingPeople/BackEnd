package com.server.delivery.domain.order.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.common.response.ResponseMessage;
import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.request.OrderUpdateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.domain.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class OrderController {
	private final OrderService orderService;

	//주문 등록 -> 주문 요청
	@PostMapping("/v1/orders")
	public CustomResponse<UUID> createOrder(
		@RequestBody OrderCreateRequestDto orderCreateRequestDto,
		@AuthenticationPrincipal CustomUserDetail userDetail
	) {
		UUID orderId = orderService.createOrder(userDetail.getUserId(), orderCreateRequestDto);
		return CustomResponse.success(ResponseMessage.ORDER_REQUEST.getMessage(), orderId);
	}

	//주문수정
	@PatchMapping("/v1/orders/{orderId}")
	public ResponseEntity<CustomResponse<?>> updateOrder(@PathVariable UUID orderId) {
		//
		CustomResponse<OrderUpdateRequestDto> responseBody = CustomResponse.success("주문 수정되었습니다");
		return ResponseEntity.ok(responseBody);
	}

	//주문조회
	@GetMapping("v1/orders/{orderId}")
	public ResponseEntity<CustomResponse<OrderGetResponseDto>> getOrder(@PathVariable UUID orderId) {
		OrderGetResponseDto response = orderService.getOrder(orderId);
		CustomResponse<OrderGetResponseDto> responseBody = CustomResponse.success("주문 조회되었습니다", response);
		return ResponseEntity.ok(responseBody);
	}

	//주문취소
	@PostMapping("v1/orders/{orderId}/cancel")
	public ResponseEntity<CustomResponse<?>> deleteOrder(@PathVariable UUID orderId) {
		orderService.deleteOrder(orderId);
		CustomResponse<?> responseBody = CustomResponse.success("주문 취소되었습니다");
		return ResponseEntity.ok(responseBody);
	}

	//주문접수
	@PostMapping("v1/orders/{orderId}/accept")
	public CustomResponse<UUID> acceptOrder(
		@RequestBody OrderAcceptRequestDto orderAcceptRequestDto,
		@PathVariable UUID orderId
	) {
		UUID deliveryId = orderService.acceptOrder(orderId, orderAcceptRequestDto);
		return CustomResponse.success("주문 접수되었습니다", deliveryId);
	}

	//주문거부
	@PostMapping("v1/orders/{orderId}/reject")
	public ResponseEntity<CustomResponse<?>> rejectOrder(
		@RequestBody OrderRejectRequestDto orderRejectRequestDto,
		@PathVariable UUID orderId
	) {
		orderService.rejectOrder(orderId, orderRejectRequestDto);
		CustomResponse<?> responseBody = CustomResponse.success("주문 거부되었습니다");
		return ResponseEntity.ok(responseBody);
	}

	// ref. 주문 완료 API 추가
}
