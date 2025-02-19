package com.server.delivery.domain.payment.service;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.payment.dto.req.PaymentCancelRequestDto;
import com.server.delivery.domain.payment.dto.req.PaymentOrderRequestDto;
import com.server.delivery.domain.payment.dto.res.PaymentCancelResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentOrderResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentSearchResponseDto;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    public PaymentOrderResponseDto requestPayment(
            Long userId,
            PaymentOrderRequestDto request
    ) {
        return null;
    }

    public PaymentCancelResponseDto cancelPayment(
            CustomUserDetail userDetail,
            String paymentId,
            PaymentCancelRequestDto request
    ) {
        return null;
    }

    public PaymentSearchResponseDto searchPayment(CustomUserDetail userId, String paymentId) {
        return null;
    }

    public PagedModel<PaymentSearchResponseDto> searchAllPayments(
            CustomUserDetail userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return null;
    }
}
