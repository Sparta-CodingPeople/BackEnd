package com.server.delivery.domain.payment.controller;

import java.time.LocalDateTime;

import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.payment.dto.req.PaymentCancelRequestDto;
import com.server.delivery.domain.payment.dto.req.PaymentOrderRequestDto;
import com.server.delivery.domain.payment.dto.res.PaymentCancelResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentOrderResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentSearchResponseDto;
import com.server.delivery.domain.payment.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
	private final PaymentService paymentService;

	// 결제 요청
	@PostMapping("/v1/payments")
	public ResponseEntity<PaymentOrderResponseDto> requestPayment(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		PaymentOrderRequestDto request
	) {
		PaymentOrderResponseDto response = paymentService.requestPayment(userDetail.getUserId(), request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// 결제 취소
	@PostMapping("/v1/payments/{paymentId}/cancel")
	public ResponseEntity<PaymentCancelResponseDto> cancelPayment(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@PathVariable String paymentId,
		PaymentCancelRequestDto request
	) {
		PaymentCancelResponseDto response = paymentService.cancelPayment(userDetail, paymentId, request);
		return ResponseEntity.ok(response);
	}

	// 결제 내역 단일 조회 (관리자, 회원)
	@GetMapping("/v1/payments/{paymentId}")
	public ResponseEntity<PaymentSearchResponseDto> searchPayment(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@PathVariable String paymentId) {
		PaymentSearchResponseDto response = paymentService.searchPayment(userDetail, paymentId);
		return ResponseEntity.ok(response);
	}

	// 결제 내역 검색 조회 (관리자, 회원)
	@GetMapping("/v1/payments")
	public ResponseEntity<PagedModel<PaymentSearchResponseDto>> searchAllPayments(
		@AuthenticationPrincipal CustomUserDetail userDetail,
		@RequestParam(value = "startDate", required = false) LocalDateTime startDate,
		@RequestParam(value = "endDate", required = false) LocalDateTime endDate
	) {
		PagedModel<PaymentSearchResponseDto> response = paymentService.searchAllPayments(userDetail, startDate,
			endDate);
		return ResponseEntity.ok(response);
	}
}
