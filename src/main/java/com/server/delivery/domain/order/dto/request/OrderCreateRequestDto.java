package com.server.delivery.domain.order.dto.request;

import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderStatus;
import com.server.delivery.model.order.entity.OrderType;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderCreateRequestDto {
    private UUID cartUuid;
    private int orderType;  //온라인주문 ref. 배달/포장
    private String orderMessage; //주문 요구사항
    private String deliveryAddress; //주문 주소

    //deliveryTip은 거리에 따라 계산되도록 하나의 정적 메서들르 만들고 사용하면 될 것 같음
    public static Order toDto(OrderCreateRequestDto requestDto, Store store, User user, Cart cart) {
        return Order.builder()
                .store(store)
                .user(user)
                .orderType(OrderType.fromNumber(requestDto.getOrderType()))
                .orderMessage(requestDto.getOrderMessage())
                .orderStatus(OrderStatus.WAITING) // ref. Order는 기본적으로 주문 승인이 되기 위한 대기 중인 상태
                .totalQuantity(cart.getTotalQuantity()) //메뉴의 총합
                .totalPrice(cart.getTotalPrice())
                .deliveryAddress(requestDto.getDeliveryAddress())
                .orderMenus(new ArrayList<>())
                .build();
    }

}

