package com.server.delivery.domain.order.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomCartException;
import com.server.delivery.common.exception.customException.CustomMenuException;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.domain.order.dto.OrderItemDto;
import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.request.OrderUpdateRequestDto;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.domain.order.dto.response.OrderSearchListResponseDto;
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
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.entity.constant.UserRole;
import com.server.delivery.util.helper.DeliveryHelper;
import com.server.delivery.util.helper.PaymentHelper;
import com.server.delivery.util.helper.UserHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderMenuRepository orderMenuRepository;
	private final MenuRepository menuRepository;
	private final CartRepository cartRepository;
	private final DeliveryHelper deliveryHelper;
	private final UserHelper userHelper;
	private final PaymentHelper paymentHelper;

	private static void updateOrderField(OrderUpdateRequestDto updateDto, Order order) {
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
	}

	private static void validateOrderStatusForUpdate(Order order) {
		if (order.getOrderStatus() == OrderStatus.DELIVERING ||
			order.getOrderStatus() == OrderStatus.CANCELED ||
			order.getOrderStatus() == OrderStatus.REJECTED
		) {
			throw new CustomOrderException(ExceptionCode.ORDER_CAN_NOT_UPDATE);
		}
	}

	private static void isOrderStoreMatchedOwnerStore(User owner, Store store) {
		if (owner.getUserStores().stream().noneMatch(userStore ->
			userStore.getStore().equals(store))) {
			throw new CustomOrderException(ExceptionCode.ORDER_STORE_OWNER_MISMATCH);
		}
	}

	private static void validateOrderStatusForAccept(Order order) {
		if (order.getOrderStatus() == OrderStatus.DELIVERING) {
			throw new CustomOrderException(ExceptionCode.ORDER_ALREADY_DELIVERING);
		} else if (order.getOrderStatus() == OrderStatus.CANCELED) {
			throw new CustomOrderException(ExceptionCode.ORDER_IS_CANCLED);
		} else if (order.getOrderStatus() == OrderStatus.ACCEPT) {
			throw new CustomOrderException(ExceptionCode.ORDER_IS_ACCEPTED);
		} else if (order.getOrderStatus() == OrderStatus.REJECTED) {
			throw new CustomOrderException(ExceptionCode.ORDER_IS_REJECTED);
		}
	}

	private static Optional<OrderMenu> isMenusExist(Order order, UUID requestMenuUuid) {
		return order.getOrderMenus().stream()
			.filter(orderMenu -> orderMenu.getMenu().getMenuUuId().equals(requestMenuUuid))
			.findFirst();
	}

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

		if (!cart.getUser().equals(user)) {
			throw new CustomOrderException(ExceptionCode.OERDER_USER_NOT_EXIST);
		}

		//order 엔티티 생성
		Order order = OrderCreateRequestDto.toDto(requestDto, store, user, cart);

		//저장, 리턴
		orderRepository.save(order);

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

	//주문 수정
	//Master만
	public void updateOrder(UUID orderId, OrderUpdateRequestDto updateDto) {
		Order order = getOrder(orderId);

		//수정가능여부 -> 주문 승인대기, 주문 승인상태에서만 가능
		validateOrderStatusForUpdate(order);

		//수정할 필드 업데이트
		updateOrderField(updateDto, order);

		// 주문 아이템 업데이트
		if (updateDto.getItems() != null && !updateDto.getItems().isEmpty()) {
			List<OrderMenu> updatedOrderMenus = new ArrayList<>();

			//각 아이템이 존재하는 아이템인지 체크 후 업데이트
			for (OrderItemDto itemDto : updateDto.getItems()) {
				//요청 메뉴 아이디 및 수량
				UUID requestMenuUuid = itemDto.getProductUuid();
				int requestMenuQuantity = itemDto.getProductCount();

				//존재하는 메뉴인지 체크
				Optional<OrderMenu> existingOrderMenu = isMenusExist(order, requestMenuUuid);
				Menu menu = getMenu(requestMenuUuid);
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
						orderMenu.setTotalPrice(requestMenuQuantity * orderMenu.getMenu().getMenuPrice());
						updatedOrderMenus.add(orderMenu);
					}
				} else {
					// 일치하는 엔티티가 없다면 새로운 주문 상품 추가
					if (requestMenuQuantity > 0) {
						OrderMenu newOrderMenu = OrderMenu.builder()
							.order(order)
							.quantity(requestMenuQuantity)
							.totalPrice(requestMenuQuantity * menu.getMenuPrice())
							.menu(menu) // 상품 조회 메서드 필요
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

	// 매장명으로 조회(일단은?)
	@Override
	public PageCustom<OrderSearchListResponseDto> searchOrder(Long userId, String search, Pageable pageable) {
		//고객 - 본인 주문만 , 사장 - 본인 매장만
		User user = userHelper.getUserById(userId);

		Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("modifiedAt"));

		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);

		Page<Order> orderPage = orderRepository.findByUserAndStoreNameContaining(user, sortedPageable, search);
		List<Order> filteredOrders;

		// 오더 검색 결과에서 오너의 매장 중 일치하는 결과들만 조회
		//TODO 유저는 조회되지만 사장은 조회안되는 문제 해결하기
		if (user.getUserRole().equals(UserRole.OWNER)) {
			filteredOrders = orderPage.getContent().stream()
				.filter(order -> user.getUserStores().stream()
					.anyMatch(userStore -> userStore.getStore().equals(order.getStore()))
				)
				.toList();
		} else {
			filteredOrders = orderPage.getContent();
		}

		// Order → OrderSearchListResponseDto 변환
		List<OrderSearchListResponseDto> responseDtoList = filteredOrders.stream()
			.map(OrderSearchListResponseDto::from) // 변환 메서드 필요
			.toList();

		// PageImpl을 사용하여 새로운 Page 객체 생성
		return new PageCustom<>(responseDtoList, sortedPageable, orderPage.getTotalElements());

	}

	//주문 조회
	@Override
	@Transactional
	public OrderGetResponseDto findOrder(UUID orderUuid, Long userId) {

		Order order = getOrder(orderUuid);
		User user = userHelper.getUserById(userId);

		//사장이라면 본인 가게의 주문만 확인
		if (user.getUserRole().equals(UserRole.OWNER)) {
			isOrderStoreMatchedOwnerStore(user, order.getStore());
		} else {
			//고객이라면 본인 주문만
			if (!Objects.equals(order.getUser().getId(), user.getId())) {
				throw new CustomOrderException(ExceptionCode.ORDER_USER_NOT_MATCHED);
			}
		}

		//OrderMenu -> orderItemDto
		List<OrderMenu> orderMenus = order.getOrderMenus();
		List<OrderItemDto> itemDtos = toItemDtos(orderMenus);

		return OrderGetResponseDto.from(order, itemDtos);
	}

	//주문접수 -> ref. 주문 승낙 여부 확인하고, 승낙된 주문만 배달정보 생성
	@Transactional
	@Override
	public void deleteOrder(UUID orderUuid, Long userId) {
		//1. 주문조회,유저 조회
		Order order = getOrder(orderUuid);
		User owner = userHelper.getUserById(userId);

		isOrderStoreMatchedOwnerStore(owner, order.getStore());

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
		order.setOrderStatus(OrderStatus.CANCELED);
		orderRepository.save(order);

		//4. item들 삭제처리
		for (OrderMenu orderitem : order.getOrderMenus()) {
			orderitem.setIsDeleted(Boolean.TRUE);
			orderitem.softDelete();
			orderMenuRepository.save(orderitem);
		}

		//TODO :: 배달 상태 변경 API를 별도로 호출하도록 설정
	}

	@Transactional
	@Override
	public void acceptOrder(UUID orderUuid, OrderAcceptRequestDto acceptDto, Long userId) {
		Order order = getOrder(orderUuid);
		Store store = order.getStore();
		User owner = userHelper.getUserById(userId);
		isOrderStoreMatchedOwnerStore(owner, store);

		//접수 불가능한 상황
		validateOrderStatusForAccept(order);

		Delivery delivery = Delivery.builder()
			.status(DeliveryStatus.WAITING)
			.deliveryTip(DeliveryTip.BASIC)
			.deliveryStartTime(acceptDto.getOrderTime())
			.deliveryArrivalTime(acceptDto.getCookingTime() + acceptDto.getEstimatedDeliveryTime())
			.build();

		deliveryHelper.save(delivery);

		order.setDelivery(delivery);
		order.setOrderStatus(OrderStatus.ACCEPT);
		order.setOrderCookingTime(acceptDto.getCookingTime());
		order.setEstimatedDeliveryTime(acceptDto.getEstimatedDeliveryTime());

		// 주문이 승인된 경우 payment와 연결 필요
		Payment foundPayment = paymentHelper.findById(acceptDto.getPaymentUuid());
		order.changePayment(foundPayment);
		orderRepository.save(order);
	}

	//주문거부
	@Transactional
	@Override
	public void rejectOrder(UUID orderUuid, OrderRejectRequestDto rejectDto, Long userId) {
		User owner = userHelper.getUserById(userId);
		Order order = getOrder(orderUuid);

		isOrderStoreMatchedOwnerStore(owner, order.getStore());

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

		// 주문이 거부된 경우 payment와 연결 필요
		Payment foundPayment = paymentHelper.findById(rejectDto.getPaymentUuid());
		order.changePayment(foundPayment);

		orderRepository.save(order);
		// TODO:: order취소시 취소 사유 테이블을 따로 만들어서 저장해야 하나?
	}

	private List<OrderItemDto> toItemDtos(List<OrderMenu> orderMenus) {
		if (orderMenus == null)
			return Collections.emptyList();

		return orderMenus.stream()
			.map(orderMenu -> OrderItemDto.builder()
				.productCount(orderMenu.getQuantity())
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

	private Cart getCart(OrderCreateRequestDto requestDto) {
		return cartRepository.findByCartUuid(requestDto.getCartUuid()).orElseThrow(
			() -> new CustomCartException(ExceptionCode.CARTS_NOT_FOUND)
		);
	}
}
