package com.server.delivery.domain.order.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.request.OrderUpdateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.domain.order.dto.response.OrderSearchListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    void createOrder(Long userId, OrderCreateRequestDto requestDto);

    OrderGetResponseDto findOrder(UUID orderUuid, Long userId);

    void deleteOrder(UUID orderUuid, Long userId);

    void acceptOrder(UUID orderUuid, OrderAcceptRequestDto acceptDto, Long userId);

    void rejectOrder(UUID orderUuid, OrderRejectRequestDto orderRejectRequestDto, Long userId);

    void updateOrder(UUID orderUuid, OrderUpdateRequestDto orderUpdateRequestDto);

    PageCustom<OrderSearchListResponseDto> searchOrder(Long userId, String search, Pageable pageable);
}
