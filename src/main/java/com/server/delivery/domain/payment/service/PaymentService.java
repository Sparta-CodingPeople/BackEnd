package com.server.delivery.domain.payment.service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.common.exception.customException.CustomPaymentException;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.payment.client.PaymentClient;
import com.server.delivery.domain.payment.client.dto.PaymentCancelOutput;
import com.server.delivery.domain.payment.client.dto.PaymentConfirmOutput;
import com.server.delivery.domain.payment.dto.req.PaymentCancelRequestDto;
import com.server.delivery.domain.payment.dto.req.PaymentConfirmRequestDto;
import com.server.delivery.domain.payment.dto.res.PaymentCancelResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentConfirmResponseDto;
import com.server.delivery.domain.payment.dto.res.PaymentSearchResponseDto;
import com.server.delivery.domain.payment.repository.PaymentJpaRepository;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.repository.OrderJpaRepository;
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderJpaRepository orderJpaRepository;
    private final UserHelper userHelper;
    private final PaymentClient paymentClient;
    private final PaymentJpaRepository paymentJpaRepository;

    @Transactional
    public PaymentConfirmResponseDto confirmPayment(CustomUserDetail userDetail,
                                                    PaymentConfirmRequestDto confirmRequest) {
        // 중복 요청 방지
        if (paymentJpaRepository.existsByOrderUuid(confirmRequest.serverOrderId())) {
            throw new CustomPaymentException(ExceptionCode.PAYMENT_REQUEST_ALREADY_DONE);
        }

        // 원래 WebClient를 통해 서버 to 서버로 결제 요청과 승인이 필요 -> 연동 없이 requestPayment, confirmPayment 진행
        PaymentConfirmOutput confirmedPaymentOutput = paymentClient.confirmPayment(confirmRequest);

        Order foundOrder = orderJpaRepository.findByOrderUuid(confirmRequest.serverOrderId())
                .orElseThrow(() -> new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND));

        User foundUser = userHelper.getUserById(userDetail.getId());

        Payment payment = Payment.builder()
                .amount(confirmedPaymentOutput.totalAmount())
                .paymentMethod(confirmedPaymentOutput.paymentMethod())
                .transactionKey(confirmedPaymentOutput.transactionKey())
                .paymentMethod(confirmedPaymentOutput.paymentMethod())
                .status(confirmedPaymentOutput.status())
                .order(foundOrder)
                .user(foundUser)
                .requestedAt(confirmedPaymentOutput.requestedAt())
                .paidAt(confirmedPaymentOutput.approvedAt())
                .build();

        paymentJpaRepository.save(payment);

        return PaymentConfirmResponseDto.from(payment);
    }

    @Transactional
    public PaymentCancelResponseDto cancelPayment(UUID paymentId, PaymentCancelRequestDto cancelRequest) {
        Payment foundPayment = paymentJpaRepository.findById(paymentId)
                .orElseThrow(() -> new CustomPaymentException(ExceptionCode.PAYMENT_NOT_FOUND));

        // 중복 요청 방지
        if (foundPayment.isCanceled()) {
            throw new CustomPaymentException(ExceptionCode.PAYMENT_ALREADY_CANCELED);
        }

        PaymentCancelOutput paymentCancelOutput = paymentClient.cancelPayment(foundPayment.getPaymentKey(),
                cancelRequest);

        foundPayment.changeCancelStatus(paymentCancelOutput);

        return PaymentCancelResponseDto.from(foundPayment);
    }

    @Transactional(readOnly = true)
    public PaymentSearchResponseDto searchPayment(UUID paymentId) {
        Payment foundPayment = paymentJpaRepository.findById(paymentId)
                .orElseThrow(() -> new CustomPaymentException(ExceptionCode.PAYMENT_NOT_FOUND));
        return PaymentSearchResponseDto.from(foundPayment);
    }

    @Transactional(readOnly = true)
    public PagedModel<PaymentSearchResponseDto> searchPayments(Long userId, Pageable pageable) {
        Page<Payment> payments = paymentJpaRepository.searchUserReviews(userId, pageable);
        Page<PaymentSearchResponseDto> content = payments.map(PaymentSearchResponseDto::from);
        return new PagedModel<>(content);
    }
}
