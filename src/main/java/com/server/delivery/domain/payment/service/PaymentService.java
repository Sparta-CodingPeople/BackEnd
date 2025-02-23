package com.server.delivery.domain.payment.service;

import com.server.delivery.common.exception.ExceptionCode;
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
import com.server.delivery.model.order.entity.OrderStatus;
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.OrderHelper;
import com.server.delivery.util.helper.PaymentHelper;
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
	private final PaymentHelper paymentHelper;

	@Transactional
	public PaymentConfirmResponseDto confirmPayment(CustomUserDetail userDetail,
		PaymentConfirmRequestDto confirmRequest) {
		// 중복 요청 방지
		if (paymentJpaRepository.existsByOrderUuid(confirmRequest.serverOrderId())) {
			throw new CustomPaymentException(ExceptionCode.PAYMENT_REQUEST_ALREADY_DONE);
		}

		// memo. Order는 기본적으로 주문 승인이 되기 위한 대기 중인 상태 (WAITING)
		// 		주문 상태가 WAITING인 경우 결제 요청 실행, 이외에는 결제 요청을 실행할 수 없음
		        Order foundOrder = orderHelper.getOrder(confirmRequest.serverOrderId());

		if (OrderStatus.isNotWaiting(foundOrder.getOrderStatus())) {
			throw new CustomPaymentException(ExceptionCode.PAYMENT_REQUEST_REJECT);
		}

		// 원래 WebClient를 통해 서버 to 서버로 결제 요청과 승인이 필요 -> 연동 없이 requestPayment, confirmPayment 진행
		PaymentConfirmOutput confirmedPaymentOutput = paymentClient.confirmPayment(confirmRequest);

		User foundUser = userHelper.getUserById(userDetail.getId());

		Payment payment = Payment.builder()
			.amount(confirmedPaymentOutput.totalAmount())
			.paymentMethod(confirmedPaymentOutput.paymentMethod())
			.transactionKey(confirmedPaymentOutput.transactionKey())
			.paymentKey(confirmedPaymentOutput.paymentKey())
			.paymentMethod(confirmedPaymentOutput.paymentMethod())
			.status(confirmedPaymentOutput.status())
			.order(foundOrder)
			.user(foundUser)
			.requestedAt(confirmedPaymentOutput.requestedAt())
			.paidAt(confirmedPaymentOutput.approvedAt())
			.build();
		
		paymentHelper.save(payment);

		return PaymentConfirmResponseDto.from(payment);
	}

	@Transactional
	public PaymentCancelResponseDto cancelPayment(UUID paymentId, PaymentCancelRequestDto cancelRequest) {
		Payment foundPayment = paymentHelper.findById(paymentId);

		// 중복 요청 방지
		if (foundPayment.isCanceled()) {
			throw new CustomPaymentException(ExceptionCode.PAYMENT_ALREADY_CANCELED);
		}

		// memo. 주문 거부(REJECTED)와 주문 취소(CANCELED)인 경우에만 결제 취소 가능
		Order order = foundPayment.getOrder();
		if (!OrderStatus.canCancelPayment(order.getOrderStatus())) {
			throw new CustomPaymentException(ExceptionCode.PAYMENT_CANCEL_FAILED);
		}

		PaymentCancelOutput paymentCancelOutput = paymentClient.cancelPayment(foundPayment.getPaymentKey(),
			cancelRequest);

		// memo. 주문 취소 시 orderStatus는 rejected로 변경되고, paymentStatus는 rejected로 설정
		foundPayment.changeCancelStatus(paymentCancelOutput);

		return PaymentCancelResponseDto.from(foundPayment);
	}

	@Transactional(readOnly = true)
	public PaymentSearchResponseDto searchPayment(UUID paymentId) {
		Payment foundPayment = paymentHelper.findById(paymentId);
		return PaymentSearchResponseDto.from(foundPayment);
	}

	@Transactional(readOnly = true)
	public PagedModel<PaymentSearchResponseDto> searchPayments(Long userId, Pageable pageable) {
		Page<Payment> payments = paymentJpaRepository.searchUserReviews(userId, pageable);
		Page<PaymentSearchResponseDto> content = payments.map(PaymentSearchResponseDto::from);
		return new PagedModel<>(content);
	}
}
