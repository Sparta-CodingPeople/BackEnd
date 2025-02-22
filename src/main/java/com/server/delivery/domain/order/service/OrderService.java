package com.server.delivery.domain.order.service;

import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.request.OrderUpdateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;

import java.util.UUID;

public interface OrderService {

    void createOrder(Long userId, OrderCreateRequestDto requestDto);

    OrderGetResponseDto findOrder(UUID orderUuid);

    void deleteOrder(UUID orderUuid);

    void acceptOrder(UUID orderUuid, OrderAcceptRequestDto acceptDto);

    void rejectOrder(UUID orderUuid, OrderRejectRequestDto orderRejectRequestDto);

    void updateOrder(UUID orderUuid, OrderUpdateRequestDto orderUpdateRequestDto);
}
