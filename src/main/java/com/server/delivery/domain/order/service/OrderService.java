package com.server.delivery.domain.order.service;

import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;

import java.util.UUID;

public interface OrderService {

    void createOrder(OrderCreateRequestDto requestDto);

//    void updateOrder();
    OrderGetResponseDto getOrder(UUID orderId);
}
