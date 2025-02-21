package com.server.delivery.domain.order.service;

import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface OrderService {

    void createOrder(OrderCreateRequestDto requestDto);

    OrderGetResponseDto getOrder(UUID orderId);

    void deleteOrder(UUID orderId);

    void acceptOrder(UUID orderId, OrderAcceptRequestDto acceptDto);

    void rejectOrder(UUID orderId, OrderRejectRequestDto orderRejectRequestDto);
}
