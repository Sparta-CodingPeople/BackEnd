package com.server.delivery.domain.order.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.domain.delivery.repository.DeliveryJpaRepository;
import com.server.delivery.domain.order.dto.OrderItemDto;
import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.request.OrderUpdateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.delivery.entity.DeliveryTip;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.order.entity.OrderMenu;
import com.server.delivery.model.order.entity.OrderStatus;
import com.server.delivery.model.order.entity.OrderType;
import com.server.delivery.model.order.repository.OrderMenuRepository;
import com.server.delivery.model.order.repository.OrderRepository;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreJpaRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final OrderMenuRepository orderMenuRepository;
	private final DeliveryJpaRepository deliveryJpaRepository;
	private final StoreJpaRepository storeJpaRepository;
	private final UserHelper userHelper;

	// ref. 주문하기 버튼을 클릭하고 주문 요청이 오면 Order 생성
	// todo. Cart 활용해서 로직 리팩토링
	//주문저장
	@Transactional
	@Override
	public UUID createOrder(Long userId, OrderCreateRequestDto requestDto) {
		//엔티티에 연결할 다른 엔티티 조회 (유저, 스토어?? -> 생성자로 생성
		//        Store store = StoreRepository.findById(requestDto.getStoreId())
		//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다"));

		//주문 받고 바로 delivery 생성하는지?? -> 주문 승낙하면 하는 게 맞지 않나??

		// ref. Store 조회
		Store foundStore = storeJpaRepository.findById(requestDto.getStoreId())
			.orElseThrow(() -> new CustomStoreException(ExceptionCode.STORE_NOT_FOUND));

		// ref. User 조회
		User user = userHelper.getUserById(userId);

		//order 엔티티 생성
		Order order = Order.builder()
			.totalQuantity(requestDto.getItems().size()) //메뉴의 총합
			.totalPrice(requestDto.getTotalPrice())
			.orderType(OrderType.valueOf(requestDto.getOrderType()))
			.store(foundStore)
			.orderMessage(requestDto.getOrderMessage())
			.orderStatus(OrderStatus.WAITING) // ref. Order는 기본적으로 주문 승인이 되기 위한 대기 중인 상태
			.deliveryAddress(requestDto.getDeliveryAddress())
			.user(user)
			//.orderStatus(OrderStatus.valueOf(requestDto.getOrderStatus()))
			//.totalPrice(requestDto.getTotalprice())
			//                .delivery(delivery)  // Todo. delivery를 order에서 생성하는지? ref. 주문 승낙 후 Delivery 생성
			.build();

		//저장, 리턴
		orderRepository.save(order);

		// OrderItemDto-> OrderItem 엔티티로 변환/ 저장
		for (OrderItemDto itemDto : requestDto.getItems()) {
			OrderMenu orderItem = OrderMenu.builder()
				//.id(String.valueOf(itemDto.getProductId())) // ref. 주문 메뉴 id는 PK
				.quantity(itemDto.getProductCount())
				.totalPrice(itemDto.getProductPrice())
				.order(order) // ref. 연관 관계 설정 필요
				.build();
			orderMenuRepository.save(orderItem);
		}

		return order.getId();
	}

	//주문조회
	@Override
	@Transactional
	public OrderGetResponseDto getOrder(UUID orderId) {
		//파라미터에서 받은 id값으로 엔티티 조회

		Order order = orderRepository.findById(orderId)
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
			//                .deliveryTip()  // todo. deliveryTip 계산은 어떻게?
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

	//주문수정
	private void updateOrder(UUID orderId, OrderUpdateRequestDto updateDto) {
		//주문조회
		//        Order order = orderRepository.findById(orderId)
		//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
		//
		//        //수정가능여부 -> 배송중이거나 취소된 주문 수정불가
		//                if (order.getOrderStatus() == OrderStatus.DELIVERING ||
		//                    order.getOrderStatus() == OrderStatus.CANCELED) {
		//                    throw new IllegalStateException("수정이 불가능한 주문 상태입니다.");
		//                };
		//        //수정할 필드 업데이트
		//        if (updateDto.getDeliveryAddress() != null){
		//            order.getDelivery().setDeliveryAddress(updateDto.getDeliveryAddress());
		//        }
		//        if (updateDto.getTotalprice() != null) {
		//            order.setTotalPrice(updateDto.getTotalprice());
		//        }
		//        if (updateDto.getMessageForRider() != null){
		//            order.setOrderMessage(updateDto.getMessageForRider());
		//        }
		//        if (updateDto.getMessageForStore() != null){
		//            order.setOrderMessage(updateDto.getMessageForStore());
		//        }
		//        if (updateDto.getUserPhoneNum() != null) {
		//            order.getUser().setPhoneNumber(updateDto.getUserPhoneNum());
		//        }
		//
		//        //item -> 아이템 일부교체
		//        if(updateDto.getItems() != null && !updateDto.getItems().isEmpty()){
		//            order.get
		//        }

		//          orderRepository.(order);

	}

	//주문취소
	@Transactional
	@Override
	public void deleteOrder(UUID orderId) {
		//1. 주문조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

		//2. 취소가능여부
		if (order.getOrderStatus() == OrderStatus.DELIVERING) {
			throw new IllegalStateException("이미 배송 중인 주문은 취소할 수 없습니다.");
		}
		if (order.getOrderStatus() == OrderStatus.CANCELED) {
			throw new IllegalStateException("이미 취소된 주문입니다.");
		}
		//3. 주문상태 변경
		order.setIsDeleted(Boolean.TRUE);
		order.setDeletedAt(LocalDateTime.now());

		//4. item들 삭제처리
		for (OrderMenu orderitem : order.getOrderMenus()) {
			orderitem.setIsDeleted(Boolean.TRUE);
			orderitem.setDeletedAt(LocalDateTime.now());
		}

		// ref. 배달 삭제 처리 -> 주문 승낙 시 배달 정보도 생성되기 때문에 주문 취소 시 배달도 삭제 필요
		order.getDelivery().isDeleted();
	}

	//주문접수 -> ref. 주문 승낙 여부 확인하고, 승낙된 주문만 배달정보 생성
	@Transactional
	@Override
	public UUID acceptOrder(UUID orderId, OrderAcceptRequestDto acceptDto) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

		//접수 불가능한 상황
		if (order.getOrderStatus() == OrderStatus.DELIVERING) {
			throw new IllegalStateException("배송중인 주문입니다");
		}
		if (order.getOrderStatus() == OrderStatus.CANCELED) {
			throw new IllegalStateException("취소된 주문입니다");
		}
		if (order.getOrderStatus() == OrderStatus.ACCEPT) {
			throw new IllegalStateException("이미 접수된 주문입니다");
		}
		if (order.getOrderStatus() == OrderStatus.REJECTED) {
			throw new IllegalStateException("이미 거부된 주문입니다");
		}

		order.setOrderStatus(OrderStatus.ACCEPT);

		// ref. 주문 승낙 후 배달 생성
		//Delivery delivery = createDelivery(order, acceptDto);

		Delivery delivery = Delivery.builder()
			.status(DeliveryStatus.WAITING)
			.deliveryTip(DeliveryTip.BASIC)
			.deliveryStartTime(acceptDto.getOrderTime())
			.deliveryArrivalTime(acceptDto.getCookingTime() + acceptDto.getEstimatedDeliveryTime())
			.build();

		deliveryJpaRepository.save(delivery);

		order.updateDelivery(delivery, acceptDto);

		return delivery.getId();
	}

	//주문거부
	@Transactional
	@Override
	public void rejectOrder(UUID orderId, OrderRejectRequestDto rejectDto) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

		//거절가능/불가능
		if (order.getOrderStatus() == OrderStatus.DELIVERING) {
			throw new IllegalStateException("이미 배송 중인 주문은 거절할 수 없습니다.");
		}
		if (order.getOrderStatus() == OrderStatus.CANCELED) {
			throw new IllegalStateException("이미 취소된 주문입니다.");
		}
		if (order.getOrderStatus() == OrderStatus.REJECTED) {
			throw new IllegalStateException("이미 거부된 주문입니다");
		}

		order.setOrderStatus(OrderStatus.REJECTED);

		// ref. 주문 취소 사유 추가
	}
}
