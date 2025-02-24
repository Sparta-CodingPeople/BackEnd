package com.server.delivery.domain.payment.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.common.response.ResponseMessage;
import com.server.delivery.domain.payment.dto.req.PaymentCancelRequestDto;
import com.server.delivery.domain.payment.dto.req.PaymentConfirmRequestDto;
import com.server.delivery.domain.payment.dto.res.PaymentCancelResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentConfirmResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentSearchResponseDto;
import com.server.delivery.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 승인
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/payments/confirm")
    public CustomResponse<PaymentConfirmResponseDto> confirmPayment(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody PaymentConfirmRequestDto request) {
        PaymentConfirmResponseDto response = paymentService.confirmPayment(userDetail, request);
        return CustomResponse.success(ResponseMessage.PAYMENT_REQUEST.getMessage(), response);
    }

    // 결제 취소
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/v1/payments/{paymentId}/cancel")
    public CustomResponse<PaymentCancelResponseDto> cancelPayment(
            @PathVariable UUID paymentId,
            @RequestBody PaymentCancelRequestDto request
    ) {
        PaymentCancelResponseDto response = paymentService.cancelPayment(paymentId, request);
        return CustomResponse.success(ResponseMessage.PAYMENT_CANCEL.getMessage(), response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/payments/{paymentId}")
    public CustomResponse<PaymentSearchResponseDto> searchPayment(@PathVariable UUID paymentId) {
        PaymentSearchResponseDto response = paymentService.searchPayment(paymentId);
        return CustomResponse.success(ResponseMessage.PAYMENT_SEARCH.getMessage(), response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1/payments")
    public CustomResponse<PagedModel<PaymentSearchResponseDto>> searchPayment(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        PagedModel<PaymentSearchResponseDto> response = paymentService.searchPayments(userDetail.getUserId(), pageable);
        return CustomResponse.success(ResponseMessage.PAYMENT_SEARCH_ALL.getMessage(), response);
    }
}
