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
    private String orderType;  //온라인주문
    private String payType;  //신용카드

    private LocalDateTime orderTime;

    private String userPhoneNum;
//    private String deliveryStatus; //배달: 준비중/배달중/배달완료
    private String orderStatus;  //주문: 완료/배송중/취소
    private String payStatus;//결제완료




}

