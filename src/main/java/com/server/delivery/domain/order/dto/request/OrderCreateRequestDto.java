package com.server.delivery.domain.order.dto.request;

import com.server.delivery.domain.order.dto.OrderItemDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderCreateRequestDto {
    private Long userId;
    private String userName;
    private Long storeId;
    private String storeName;
    private List<OrderItemDto> items;

    private int deliveryTip;
    private int totalprice;
    private String deliveryAddress;
    private String messageForRider;
    private String messageForRestorant;
    private String orderType;
    private String payType;

    private LocalDateTime orderTime;

    private String userPhoneNum;
    private String deliveryStatus;
    private String payStatus;
}

