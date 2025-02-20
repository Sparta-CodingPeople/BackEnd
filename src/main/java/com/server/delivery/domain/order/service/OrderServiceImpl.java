package com.server.delivery.domain.order.service;

import com.server.delivery.domain.order.dto.OrderItemDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.model.order.entity.*;
import com.server.delivery.model.order.repository.OrderMenuRepository;
import com.server.delivery.model.order.repository.OrderRepository;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.util.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final UserHelper userHelper;

    //주문저장
    @Transactional
    @Override
    public void createOrder(OrderCreateRequestDto requestDto) {
        //엔티티에 연결할 다른 엔티티 조회 (유저, 스토어?? -> 생성자로 생성
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다"));

        //주문 받고 바로 delivery 생성하는지?? -> 주문 승낙하면 하는 게 맞지 않나??

        //order 엔티티 생성
        Order order = Order.builder()
                .totalPrice(requestDto.getTotalprice())
                .totalQuantity(requestDto.getItems().size()) //메뉴의 총합
                .orderType(OrderType.valueOf(requestDto.getOrderType()))
                .orderStatus(OrderStatus.valueOf(requestDto.getOrderStatus()))
//                .delivery(delivery)  // Todo. delivery를 order에서 생성하는지?
                .store(store)
                .build();

        //저장, 리턴
        orderRepository.save(order);

        // OrderItemDto-> OrderItem 엔티티로 변환/ 저장
        for (OrderItemDto itemDto : requestDto.getItems()) {
            OrderMenu orderItem = OrderMenu.builder()
                    .menuId(String.valueOf(itemDto.getProductId()))
                    .quantity(itemDto.getProductCount())
                    .totalPrice(itemDto.getProductPrice())
                    .build();
            orderMenuRepository.save(orderItem);
        }
    }

    //주문조회
    @Override
    @Transactional
    public OrderGetResponseDto getOrder(UUID orderId){
        //파라미터에서 받은 id값으로 엔티티 조회

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        //OrderMenu -> orderItemDto
        List<OrderMenu> orderMenus = order.getOrderMenus();
        List<OrderItemDto> itemDtos = toItemDtos(orderMenus);

        OrderGetResponseDto responseDto = OrderGetResponseDto.builder()
                .userId(order.getUser().getId())
                .userName(order.getUser().getUsername())
                .storeId(order.getStore().getStoreUuid())
                .storeName(order.getStore().getStoreName())
                .items(itemDtos)
//                .deliveryTip()  // todo. deliveryTip 계산은 어떻게?
                .totalprice(order.getTotalPrice())
                .deliveryAddress(order.getDelivery().getDeliveryAddress())
                .messageForRider(order.getDelivery().getDeliveryMemo())
                .messageForStore(order.getOrderMessage()) //메세지
                .orderType(String.valueOf(order.getOrderType()))
                .payType(String.valueOf(order.getPayment().getPaymentMethod()))
                .orderTime(order.getCreatedAt())
                .userPhoneNum(order.getUser().getPhoneNumber())
                .orderStatus(String.valueOf(order.getOrderStatus()))
                .deliveryStartTime(order.getDelivery().getDeliveryStartTime())
                .payStatus(String.valueOf(order.getPayment().getStatus()))
                .build();

        return responseDto;
    }

    private List<OrderItemDto> toItemDtos(List<OrderMenu> orderMenus) {
        if (orderMenus == null) return Collections.emptyList();

        return orderMenus.stream()
                .map(orderMenu -> OrderItemDto.builder()
                        .productId(UUID.fromString(orderMenu.getMenuId()))
                        .productCount(orderMenu.getQuantity())
                        .productPrice(orderMenu.getTotalPrice())
                        .build()
                )
                .toList();
    }



}
