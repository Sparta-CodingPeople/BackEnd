package com.server.delivery.domain.order.dto.request;

import com.server.delivery.domain.order.dto.OrderItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderUpdateRequestDto {

    private List<OrderItemDto> items;
    private int deliveryTip;
    private String deliveryAddress;
    private String messageForRider;
    private String messageForStore;
    private String userPhoneNum;

}
