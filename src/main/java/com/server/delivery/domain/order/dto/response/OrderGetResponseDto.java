package com.server.delivery.domain.order.dto.response;

import com.server.delivery.domain.order.dto.OrderItemDto;
import com.server.delivery.model.order.entity.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderGetResponseDto {
    private Long userId;
    private String userName;
    private UUID storeId;
    private String storeName;
    private List<OrderItemDto> items;
    private int deliveryTip;
    private int totalprice;
    private String deliveryAddress;
    private String messageForRider;
    private String messageForStore;
    private String orderType;
    private String payType;
    private LocalDateTime orderTime;
    private String userPhoneNum;
    private String orderStatus;
    private LocalDateTime deliveryStartTime;
    private String payStatus;

    public static OrderGetResponseDto from(Order order, List<OrderItemDto> itemDtos) {
        return OrderGetResponseDto.builder()
                .userId(order.getUser().getId())
                .userName(order.getUser().getUsername())
                .storeId(order.getStore().getStoreUuid())
                .storeName(order.getStore().getStoreName())
                .items(itemDtos)
                .totalprice(order.getTotalPrice())
                .deliveryAddress(order.getDeliveryAddress())
                .messageForStore(order.getOrderMessage()) //메세지
                .orderType(String.valueOf(order.getOrderType()))
                .payType(String.valueOf(order.getPayment().getPaymentMethod()))
                .orderTime(order.getCreatedAt())
                .userPhoneNum(order.getUser().getPhoneNumber())
                .orderStatus(String.valueOf(order.getOrderStatus()))
                .deliveryStartTime(order.getDelivery().getDeliveryStartTime())
                .payStatus(String.valueOf(order.getPayment().getStatus()))
                .build();
    }
}
