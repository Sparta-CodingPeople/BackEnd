package com.server.delivery.util.helper;

import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OrderHelper {
    private final OrderRepository orderRepository;

    public Order getOrder(UUID orderId) {
        return orderRepository.findByOrderUuid(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
