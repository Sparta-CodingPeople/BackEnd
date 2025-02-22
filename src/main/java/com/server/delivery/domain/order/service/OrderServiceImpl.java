package com.server.delivery.domain.order.service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomCartException;
import com.server.delivery.common.exception.customException.CustomMenuException;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.domain.delivery.repository.DeliveryJpaRepository;
import com.server.delivery.domain.order.dto.OrderItemDto;
import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.request.OrderUpdateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.cart.repository.CartRepository;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.delivery.entity.DeliveryTip;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.menu.repository.MenuRepository;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderMenu;
import com.server.delivery.model.order.entity.OrderStatus;
import com.server.delivery.model.order.repository.OrderMenuRepository;
import com.server.delivery.model.order.repository.OrderRepository;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final UserHelper userHelper;


    // ref. 주문하기 버튼을 클릭하고 주문 요청이 오면 Order 생성
    //주문요청
    @Transactional
    @Override
    public void createOrder(Long userId, OrderCreateRequestDto requestDto) {
        Store store = null;
        Cart cart = getCart(requestDto);
        if (!cart.getMenuCarts().isEmpty()) {
            store = cart.getMenuCarts().get(0).getMenu().getStore();
        }

        //주문자
        User user = userHelper.getUserById(userId);

        //order 엔티티 생성
        Order order = OrderCreateRequestDto.toDto(requestDto, store, user, cart);

        //저장, 리턴
        Order savedOrder = orderRepository.save(order);

        List<OrderMenu> orderMenuList = cart.getMenuCarts().stream()
                .map(menuCart -> {
                    Menu menuByOrderMenu = menuRepository.findByMenuUuId(menuCart.getMenu().getMenuUuId())
                            .orElseThrow(
                                    () -> new CustomMenuException(ExceptionCode.MENU_NOT_FOUND)
                            );

                    return OrderMenu.builder()
                            .quantity(cart.getTotalQuantity())
                            .totalPrice(cart.getTotalPrice())
                            .order(order)
                            .menu(menuByOrderMenu)
                            .build();
                }).toList();

        List<OrderMenu> savedOrderMenuList = orderMenuRepository.saveAll(orderMenuList);

        order.getOrderMenus().addAll(savedOrderMenuList);
    }

    private Cart getCart(OrderCreateRequestDto requestDto) {
        return cartRepository.findByCartUuid(requestDto.getCartUuid()).orElseThrow(
                () -> new CustomCartException(ExceptionCode.CARTS_NOT_FOUND)
        );
    }

    //주문수정
    public void updateOrder(UUID orderId, OrderUpdateRequestDto updateDto) {
        Order order = getOrder(orderId);

        //수정가능여부 -> 주문 승인대기, 주문 승인상태에서만 가능
        if (order.getOrderStatus() == OrderStatus.DELIVERING ||
                order.getOrderStatus() == OrderStatus.CANCELED ||
                order.getOrderStatus() == OrderStatus.REJECTED
        ) {
            throw new CustomOrderException(ExceptionCode.ORDER_CAN_NOT_UPDATE);
        }

        //수정할 필드 업데이트

        if (updateDto.getDeliveryAddress() != null) {
            order.setDeliveryAddress(updateDto.getDeliveryAddress());
        }
        if (updateDto.getMessageForRider() != null) {
            order.setOrderMessage(updateDto.getMessageForRider());
        }
        if (updateDto.getMessageForStore() != null) {
            order.setOrderMessage(updateDto.getMessageForStore());
        }
        if (updateDto.getUserPhoneNum() != null) {
            order.getUser().setPhoneNumber(updateDto.getUserPhoneNum());
        }

        // 주문 아이템 업데이트
        if (updateDto.getItems() != null && !updateDto.getItems().isEmpty()) {
            List<OrderMenu> updatedOrderMenus = new ArrayList<>();

            //각 아이템이 존재하는 아이템인지 체크 후 업데이트
            for (OrderItemDto itemDto : updateDto.getItems()) {
                //요청 메뉴 아이디 및 수량
                UUID requestMenuUuid = itemDto.getProductId();
                int requestMenuQuantity = itemDto.getProductCount();

                //존재하는 메뉴인지 체크
                Optional<OrderMenu> existingOrderMenu = order.getOrderMenus().stream()
                        .filter(orderMenu -> orderMenu.getMenu().getMenuUuId().equals(requestMenuUuid))
                        .findFirst();

                //존재할경우 데이터 수정
                if (existingOrderMenu.isPresent()) {
                    OrderMenu orderMenu = existingOrderMenu.get();
                    //만약 0개라면
                    if (requestMenuQuantity == 0) {
                        // 수량이 0이면 주문에서 제거
                        order.getOrderMenus().remove(orderMenu);
                    } else {
                        // 0개 이상이라면 수량 및 가격 업데이트
                        orderMenu.setQuantity(requestMenuQuantity);
                        orderMenu.setTotalPrice(requestMenuQuantity * itemDto.getProductPrice());
                        updatedOrderMenus.add(orderMenu);
                    }
                } else {
                    // 일치하는 엔티티가 없다면 새로운 주문 상품 추가
                    if (requestMenuQuantity > 0) {
                        OrderMenu newOrderMenu = OrderMenu.builder()
                                .order(order)
                                .quantity(requestMenuQuantity)
                                .totalPrice(requestMenuQuantity * itemDto.getProductPrice())
                                .menu(getMenu(requestMenuUuid)) // 상품 조회 메서드 필요
                                .build();
                        updatedOrderMenus.add(newOrderMenu);
                    }
                }
            }

            // 수정된 주문 메뉴 리스트 반영
            order.setOrderMenus(updatedOrderMenus);
        }

        // 총 주문 금액 및 수량 업데이트
        int totalPrice = order.getOrderMenus().stream()
                .mapToInt(OrderMenu::getTotalPrice)
                .sum();
        int totalQuantity = order.getOrderMenus().stream()
                .mapToInt(OrderMenu::getQuantity)
                .sum();

        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(totalQuantity);
    }


    //주문조회
    @Override
    @Transactional
    public OrderGetResponseDto findOrder(UUID orderUuid) {
        //파라미터에서 받은 id값으로 엔티티 조회

        Order order = orderRepository.findByOrderUuid(orderUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        //OrderMenu -> orderItemDto
        List<OrderMenu> orderMenus = order.getOrderMenus();
        List<OrderItemDto> itemDtos = toItemDtos(orderMenus);

        return OrderGetResponseDto.builder()
                .userId(order.getUser().getId())
                .userName(order.getUser().getUsername())
                .storeId(order.getStore().getStoreUuid())
                .storeName(order.getStore().getStoreName())
                .items(itemDtos)
                .totalprice(order.getTotalPrice())
                //.deliveryAddress(order.getDelivery().getDeliveryAddress())
                .deliveryAddress(order.getDeliveryAddress())
                //.messageForRider(order.getDelivery().getDeliveryMemo())
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


    //주문취소
    @Transactional
    @Override
    public void deleteOrder(UUID orderUuid) {
        //1. 주문조회
        Order order = getOrder(orderUuid);

        //2. 취소가능여부
        if (order.getOrderStatus() == OrderStatus.DELIVERING) {
            throw new CustomOrderException(ExceptionCode.ORDER_ALREADY_DELIVERING);
        }
        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new CustomOrderException(ExceptionCode.ORDER_IS_CANCLED);
        }
        //3. 주문상태 변경
        order.setIsDeleted(Boolean.TRUE);
        order.softDelete();

        //4. item들 삭제처리
        for (OrderMenu orderitem : order.getOrderMenus()) {
            orderitem.setIsDeleted(Boolean.TRUE);
            orderitem.softDelete();
        }

        //TODO :: 배달 상태 변경 API를 별도로 호출하도록 설정
    }

    //주문접수 -> ref. 주문 승낙 여부 확인하고, 승낙된 주문만 배달정보 생성
    @Transactional
    @Override
    public void acceptOrder(UUID orderUuid, OrderAcceptRequestDto acceptDto) {
        Order order = getOrder(orderUuid);

        //접수 불가능한 상황
        if (order.getOrderStatus() == OrderStatus.DELIVERING) {
            throw new CustomOrderException(ExceptionCode.ORDER_ALREADY_DELIVERING);
        } else if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new CustomOrderException(ExceptionCode.ORDER_IS_CANCLED);
        } else if (order.getOrderStatus() == OrderStatus.ACCEPT) {
            throw new CustomOrderException(ExceptionCode.ORDER_IS_ACCEPTED);
        } else if (order.getOrderStatus() == OrderStatus.REJECTED) {
            throw new CustomOrderException(ExceptionCode.ORDER_IS_REJECTED);
        }

        Delivery delivery = Delivery.builder()
                .status(DeliveryStatus.WAITING)
                .deliveryTip(DeliveryTip.BASIC)
                .deliveryStartTime(acceptDto.getOrderTime())
                .deliveryArrivalTime(acceptDto.getCookingTime() + acceptDto.getEstimatedDeliveryTime())
                .build();

        deliveryJpaRepository.save(delivery);

        order.setDelivery(delivery);
        order.setOrderStatus(OrderStatus.ACCEPT);
        order.setOrderCookingTime(acceptDto.getCookingTime());
        order.setEstimatedDeliveryTime(acceptDto.getEstimatedDeliveryTime());

    }

    //주문거부
    @Transactional
    @Override
    public void rejectOrder(UUID orderUuid, OrderRejectRequestDto rejectDto) {
        Order order = getOrder(orderUuid);

        //거절가능/불가능
        if (order.getOrderStatus() == OrderStatus.DELIVERING) {
            throw new CustomOrderException(ExceptionCode.ORDER_ALREADY_DELIVERING);
        }
        if (order.getOrderStatus() == OrderStatus.CANCELED) {
            throw new CustomOrderException(ExceptionCode.ORDER_IS_CANCLED);
        }
        if (order.getOrderStatus() == OrderStatus.REJECTED) {
            throw new CustomOrderException(ExceptionCode.ORDER_IS_REJECTED);
        }

        order.setOrderStatus(OrderStatus.REJECTED);

        orderRepository.save(order);

        // TODO:: order취소시 취소 사유 테이블을 따로 만들어서 저장해야 하나?
    }


    private List<OrderItemDto> toItemDtos(List<OrderMenu> orderMenus) {
        if (orderMenus == null)
            return Collections.emptyList();

        return orderMenus.stream()
                .map(orderMenu -> OrderItemDto.builder()
                        //.productId(UUID.fromString(orderMenu.getId()))
                        .productCount(orderMenu.getQuantity())
                        .productPrice(orderMenu.getTotalPrice())
                        .build()
                )
                .toList();
    }

    private Menu getMenu(UUID requestMenuUuid) {
        return menuRepository.findByMenuUuId(requestMenuUuid).orElseThrow(
                () -> new CustomMenuException(ExceptionCode.MENU_NOT_FOUND)
        );
    }


    private Order getOrder(UUID orderId) {
        return orderRepository.findByOrderUuid(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }


}
