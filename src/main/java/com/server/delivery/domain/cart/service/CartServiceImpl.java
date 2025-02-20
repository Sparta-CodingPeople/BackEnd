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

    @Override
    @Transactional
    public void createCart(Long userId, CreateCartRequestDto createCartRequestDto) {
        //장바구니는 누구 것?
        User user = userHelper.getUserById(userId);

        int quantity = createCartRequestDto.getQuantity();
        cartRepository.findByUser(user).ifPresentOrElse(cart -> {
                    // 1. 이미 장바구니에 내용이 있다면 예외 반환 ->  클라이언트에서 알림 -> 장바구니 비우는 API 호출하는 시나리오
                    if (!cart.getMenuCarts().isEmpty()) throw new CustomCartException(ExceptionCode.CARTS_ORDER_ITEM_EXIST);
                        // 장바구니에 내용이 없으면 새로 메뉴 추가
                        // createCartRequestDto에서 메뉴와 수량 등을 받아서 처리
                    else {
                        Menu menu = getMenu(createCartRequestDto);
                        MenuCart newMenuCart = createCartRequestDto.to(menu, cart, quantity);

                        menuCartRepository.save(newMenuCart);

                        //장바구니 업데이트 ( 가격, 수량)
                        cart.setTotalPrice(cart.getTotalPrice() + newMenuCart.getTotalPrice());
                        cart.setTotalQuantity(cart.getTotalQuantity() + newMenuCart.getQuantity());
                        cartRepository.save(cart);
                    }
                },
                () -> {
                    // 장바구니가 없으면 새로 생성
                    Menu menu = getMenu(createCartRequestDto);
                    Cart cart = Cart.builder()
                            .user(user)
                            .menuCarts(new ArrayList<>())
                            .totalPrice(menu.getMenuPrice() * quantity)
                            .totalQuantity(quantity)
                            .build();

                    cartRepository.save(cart);

                    //새 장바구니에 메뉴 추가
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

    private Menu getMenu(CreateCartRequestDto createCartRequestDto) {
        return menuRepository.findByMenuUuId(createCartRequestDto.getProductId()).orElseThrow(
                () -> new CustomMenuException(ExceptionCode.MENU_NOT_FOUND)

        );

    }
}
