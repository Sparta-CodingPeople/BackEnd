package com.server.delivery.domain.cart.service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomCartException;
import com.server.delivery.common.exception.customException.CustomMenuException;
import com.server.delivery.domain.cart.dto.CartItemDto;
import com.server.delivery.domain.cart.dto.request.CreateCartRequestDto;
import com.server.delivery.domain.cart.dto.request.UpdateCartRequestDto;
import com.server.delivery.domain.cart.dto.response.SearchCartResponseDto;
import com.server.delivery.model.cart.entity.Cart;
import com.server.delivery.model.cart.entity.MenuCart;
import com.server.delivery.model.cart.repository.CartRepository;
import com.server.delivery.model.cart.repository.MenuCartRepository;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.menu.repository.MenuRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.MenuHelper;
import com.server.delivery.util.helper.UserHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final MenuCartRepository menuCartRepository;
    private final UserHelper userHelper;
    private final MenuHelper menuHelper;

    private static boolean isCartMenuEqualsRequestMenuStore(Cart cart, Menu menu) {
        boolean isCartMenuEqualsRequestMenuStore = cart.getMenuCarts().stream().anyMatch(
                menucart -> menucart.getMenu().getStore().getStoreUuid().equals(menu.getStore().getStoreUuid())
        );
        if (isCartMenuEqualsRequestMenuStore) {
        }
        return isCartMenuEqualsRequestMenuStore;
    }

    @Override
    @Transactional
    public void createCart(Long userId, CreateCartRequestDto createCartRequestDto) {
        // 장바구니는 누구 것?
        User user = userHelper.getUserById(userId);

        int quantity = createCartRequestDto.getQuantity();
        Menu menu = menuHelper.getMenu(createCartRequestDto.getMenuUuid());


        cartRepository.findByUser(user).ifPresentOrElse(cart -> {
                    // 1. 이미 장바구니에 내용이 있고 현재 주문하는 메뉴와 장바구니의 메뉴가 다른 Store것이라면 예외 반환 -> 클라이언트에서 알림 -> 장바구니 비우는 API 호출하는 시나리오
                    // 장바구니에 내용은 있으나 동일한 스토어인 경우 같은 제품인지 확인
                    if (!cart.getMenuCarts().isEmpty() && !isCartMenuEqualsRequestMenuStore(cart, menu)) {
                        throw new CustomCartException(ExceptionCode.CARTS_ORDER_ITEM_EXIST);
                    } else if (isCartMenuEqualsRequestMenuStore(cart, menu)) {
                        // 동일한 스토어인 경우: 장바구니에 같은 메뉴가 있다면 수량만 업데이트
                        cart.getMenuCarts().stream()
                                .filter(menucart -> menucart.getMenu().equals(menu)) // 동일한 메뉴 찾기
                                .findFirst()
                                .ifPresentOrElse(existingMenuCart -> {
                                    // 기존 메뉴가 있으면 수량만 변경
                                    int oldQuantity = existingMenuCart.getQuantity(); // 기존 수량
                                    int newTotalPrice = menu.getMenuPrice() * (existingMenuCart.getQuantity() + quantity);

                                    existingMenuCart.setQuantity(existingMenuCart.getQuantity() + quantity);
                                    existingMenuCart.setTotalPrice(newTotalPrice);
                                    menuCartRepository.save(existingMenuCart);

                                    // 장바구니 업데이트 (가격, 수량)
                                    cart.setTotalPrice(cart.getTotalPrice() + menu.getMenuPrice() * quantity);
                                    cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
                                    cartRepository.save(cart);
                                }, () -> {
                                    // 기존 메뉴가 없으면 새로 메뉴 추가
                                    MenuCart newMenuCart = createCartRequestDto.to(menu, cart, quantity);
                                    menuCartRepository.save(newMenuCart);

                                    // 장바구니 업데이트 (가격, 수량)
                                    cart.setTotalPrice(cart.getTotalPrice() + newMenuCart.getTotalPrice());
                                    cart.setTotalQuantity(cart.getTotalQuantity() + newMenuCart.getQuantity());
                                    cartRepository.save(cart);
                                });
                    }
                },
                () -> {
                    // 장바구니가 없으면 새로 생성
                    Cart cart = Cart.builder()
                            .user(user)
                            .menuCarts(new ArrayList<>())
                            .totalPrice(menu.getMenuPrice() * quantity)
                            .totalQuantity(quantity)
                            .build();

                    cartRepository.save(cart);

                    // 새 장바구니에 메뉴 추가
                    MenuCart menuCart = MenuCart.builder()
                            .menu(menu)
                            .cart(cart)
                            .quantity(quantity)
                            .totalPrice(quantity * menu.getMenuPrice())
                            .build();

                    menuCartRepository.save(menuCart);
                });
    }

    @Override
    @Transactional
    public SearchCartResponseDto searchCart(Long userId) {
        User user = userHelper.getUserById(userId);

        Cart cart = getCart(user);

        //메뉴와 장바구니 정보를 이용해 응답 DTO 반환
        List<CartItemDto> cartItemDtos = cart.getMenuCarts().stream()
                .map(CartItemDto::from)
                .toList();

        int totalPrice = cart.getMenuCarts().stream()
                .mapToInt(MenuCart::getTotalPrice)
                .sum();

        int totalQuantity = cart.getMenuCarts().stream()
                .mapToInt(MenuCart::getQuantity)  // MenuCart에서 quantity 값 추출
                .sum();  // 총 합계


        return SearchCartResponseDto.from(totalPrice, totalQuantity, cartItemDtos, cart.getCartUuid());
    }

    private Cart getCart(User user) {
        return cartRepository.findByUser(user).orElseThrow(
                () -> new CustomCartException(ExceptionCode.CARTS_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public void deleteCart(Long userId) {
        User user = userHelper.getUserById(userId);
        Cart cart = getCart(user);

        cart.softDelete();
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void updateCart(Long userId, UpdateCartRequestDto updateCartRequestDto, UUID productId) {
        User user = userHelper.getUserById(userId);
        Cart cart = getCart(user);
        MenuCart menuCart = getMenuCart(productId);

        int oldQuantity = menuCart.getQuantity(); // 기존 수량
        int newQuantity = updateCartRequestDto.getQuantity(); // 변경할 수량
        int menuPrice = menuCart.getMenu().getMenuPrice(); // 메뉴 단가

        // 수량이 0이면 삭제 처리
        if (newQuantity == 0) {
            cart.setTotalQuantity(cart.getTotalQuantity() - oldQuantity); // 기존 수량 차감
            cart.setTotalPrice(cart.getTotalPrice() - menuCart.getTotalPrice()); // 기존 가격 차감
            menuCartRepository.delete(menuCart);
        } else {
            // 기존 총 가격에서 기존 메뉴 가격을 빼고, 새로운 가격을 추가
            cart.setTotalPrice(cart.getTotalPrice() - menuCart.getTotalPrice() + (newQuantity * menuPrice));

            // 기존 총 수량에서 기존 수량을 빼고, 새로운 수량을 추가
            cart.setTotalQuantity(cart.getTotalQuantity() - oldQuantity + newQuantity);

            // MenuCart 업데이트
            menuCart.setQuantity(newQuantity);
            menuCart.setTotalPrice(newQuantity * menuPrice);
            menuCartRepository.save(menuCart);
        }
    }

    private MenuCart getMenuCart(UUID productId) {
        return menuCartRepository.findByMenuUuid(productId).orElseThrow(
                () -> new CustomMenuException(ExceptionCode.MENU_CART_NOT_FOUND)
        );
    }
}
