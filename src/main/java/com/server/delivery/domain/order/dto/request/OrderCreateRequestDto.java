package com.server.delivery.domain.order.dto.request;

import com.server.delivery.domain.order.dto.OrderItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderCreateRequestDto {
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
    private String deliveryStatus;
    private String payStatus;
}

