package com.server.delivery.domain.order.service;

import java.util.UUID;

import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;

public interface OrderService {

	UUID createOrder(Long userId, OrderCreateRequestDto requestDto);

	OrderGetResponseDto getOrder(UUID orderId);

	void deleteOrder(UUID orderId);

	UUID acceptOrder(UUID orderId, OrderAcceptRequestDto acceptDto);

	void rejectOrder(UUID orderId, OrderRejectRequestDto orderRejectRequestDto);
}
