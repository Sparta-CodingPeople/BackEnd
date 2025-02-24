package com.server.delivery.domain.delivery.dto.res;

import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderMenu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DeliverySearchResponseDto(
        OrderInfo orderInfo,
        DeliveryInfo deliveryInfo
) {
    public static DeliverySearchResponseDto of(Order order, Delivery delivery) {
        return new DeliverySearchResponseDto(
                OrderInfo.from(order),
                DeliveryInfo.from(delivery)
        );
    }

    public record OrderInfo(
            UUID orderId,
            String orderer,
            String contact,
            String orderMessage,
            List<OrderItems> orderItems,
            LocalDateTime orderCreatedAt
    ) {

        public static OrderInfo from(Order order) {
            return new OrderInfo(
                    order.getOrderUuid(),
                    order.getUser().getUsername(),
                    order.getUser().getPhoneNumber(),
                    order.getOrderMessage(),
                    getOrderItems(order.getOrderMenus()),
                    order.getCreatedAt()
            );
        }

        private static List<OrderItems> getOrderItems(List<OrderMenu> orderMenus) {
            return orderMenus.stream().map(OrderItems::from).toList();
        }

        public record OrderItems(String menuName) {
            public static OrderItems from(OrderMenu orderMenu) {
                return new OrderItems(orderMenu.getMenu().getMenuName());
            }
        }
    }

    public record DeliveryInfo(
            UUID deliveryId,
            LocalDateTime deliveryCreatedAt,
            DeliveryStatus deliveryStatus,
            LocalDateTime deliveryStartTime,
            int estimatedDeliveryTime
    ) {
        public static DeliveryInfo from(Delivery delivery) {
            return new DeliveryInfo(
                    delivery.getDeliveryUuid(),
                    delivery.getCreatedAt(),
                    delivery.getStatus(),
                    delivery.getDeliveryStartTime(),
                    delivery.getDeliveryArrivalTime()
            );
        }
    }
}
