package com.server.delivery.domain.payment.client;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomPaymentException;
import com.server.delivery.domain.payment.client.dto.PaymentCancelOutput;
import com.server.delivery.domain.payment.client.dto.PaymentConfirmOutput;
import com.server.delivery.domain.payment.dto.req.PaymentCancelRequestDto;
import com.server.delivery.domain.payment.dto.req.PaymentConfirmRequestDto;
import com.server.delivery.model.payment.PaymentMethod;
import com.server.delivery.model.payment.PaymentStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PaymentClient {
	/**
	 *
	 * @param confirmRequest  paymentKey, serverOrderId, amount (successUrl의 쿼리파라미터)
	 * @return paymentKey, transactionKey,serverOrderId, amount, method, requestedAt, approvedAt  (Payment 객체에서 필요한 부분 추출)
	 */
	public PaymentConfirmOutput confirmPayment(PaymentConfirmRequestDto confirmRequest) {
		try {
			return new PaymentConfirmOutput(
				generatePaymentKey(),
				confirmRequest.serverOrderId(),
				generateLastTransactionKey(),
				confirmRequest.amount(),
				PaymentMethod.CREDIT_CARD,
				ZonedDateTime.now(),
				ZonedDateTime.now(),
				PaymentStatus.COMPLETED
			);
		} catch (ResourceAccessException e) {
			log.error("paymentClient confirm api failed: " + e.getMessage());
			throw new CustomPaymentException(ExceptionCode.PAYMENT_CONFIRM_ERROR);
		}
	}

	/**
	 *
	 * @param paymentKey
	 * @param cancelRequest 취소 사유 (필수)
	 * @return lastTransactionKey, status, canceledAt, cancelReason, cancelTransactionKey (Payment 객체에서 필요한 데이터 추출)
	 */
	public PaymentCancelOutput cancelPayment(String paymentKey, PaymentCancelRequestDto cancelRequest) {
		if (cancelRequest.cancelReason() == null) {
			throw new CustomPaymentException(ExceptionCode.PAYMENT_CANCEL_REASON_NOT_FOUND);
		}

		try {
			return new PaymentCancelOutput(
				paymentKey,
				generateLastTransactionKey(),
				PaymentStatus.CANCELLED,
				new PaymentCancelOutput.Cancels(
					generateCancelTransactionKey(),
					cancelRequest.cancelReason(),
					ZonedDateTime.now()
				)
			);
		} catch (ResourceAccessException e) {
			log.error("paymentClient cancel api failed: " + e.getMessage());
			throw new CustomPaymentException(ExceptionCode.PAYMENT_CANCEL_ERROR);
		}
	}

	private String generatePaymentKey() {
		return UUID.randomUUID().toString();
	}

	private String generateLastTransactionKey() {
		return UUID.randomUUID().toString();
	}

	private String generateCancelTransactionKey() {
		return UUID.randomUUID().toString();
	}
}
