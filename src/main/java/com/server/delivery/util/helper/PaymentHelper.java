package com.server.delivery.util.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomPaymentException;
import com.server.delivery.domain.payment.repository.PaymentJpaRepository;
import com.server.delivery.model.payment.Payment;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentHelper {
	private final PaymentJpaRepository paymentJpaRepository;

	public Payment save(Payment payment) {
		return paymentJpaRepository.save(payment);
	}

	public Payment findById(UUID paymentId) {
		return paymentJpaRepository.findById(paymentId)
			.orElseThrow(() -> new CustomPaymentException(ExceptionCode.PAYMENT_NOT_FOUND));
	}
}
