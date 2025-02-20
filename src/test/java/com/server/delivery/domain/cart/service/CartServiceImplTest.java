package com.server.delivery.domain.cart.service;


import com.server.delivery.common.exception.customException.CustomCartException;
import com.server.delivery.common.exception.customException.CustomMenuException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuCartRepository menuCartRepository;

    @Mock
    private UserHelper userHelper;

    private User user;
    private Menu menu;
    private Cart cart;
    private UUID menuUuid;
    private UUID cartUuid;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testUser").build();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUser", null, List.of());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        menuUuid = UUID.randomUUID();
        cartUuid = UUID.randomUUID();
        menu = Menu.builder().menuUuId(menuUuid).menuPrice(10000).build();
        cart = Cart.builder()
                .cartUuid(cartUuid)
                .user(user)
                .menuCarts(new ArrayList<>())
                .totalPrice(0)
                .totalQuantity(0)
                .build();
    }

    @Test
    @DisplayName("장바구니 생성 성공")
    void 장바구니_생성_성공() {
        CreateCartRequestDto requestDto = CreateCartRequestDto.builder().productId(menuUuid).quantity(2).build();
        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(menuRepository.findByMenuUuId(menuUuid)).thenReturn(Optional.of(menu));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(menuCartRepository.save(any(MenuCart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        cartService.createCart(1L, requestDto);

        // then
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(menuCartRepository, times(1)).save(any(MenuCart.class));
    }

    @Test
    void 장바구니_조회_성공() {
        // given
        MenuCart menuCart = MenuCart.builder()
                .menu(menu)
                .cart(cart)
                .quantity(2)
                .totalPrice(20000)
                .build();
        cart.getMenuCarts().add(menuCart);
        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // when
        SearchCartResponseDto responseDto = cartService.searchCart(1L);

        // then
        assertThat(responseDto.getTotalPrice()).isEqualTo(20000);
        assertThat(responseDto.getTotalQuantity()).isEqualTo(2);
        assertThat(responseDto.getItems()).hasSize(1);
    }

    @Test
    void 장바구니_삭제_성공() {
        // given
        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // when
        cartService.deleteCart(1L);

        // then
        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    void 장바구니_업데이트_성공() {
        // given
        MenuCart menuCart = MenuCart.builder()
                .menu(menu)
                .cart(cart)
                .quantity(2)
                .totalPrice(20000)
                .build();
        cart.getMenuCarts().add(menuCart);

        UUID productId = menuCart.getMenu().getMenuUuId();
        UpdateCartRequestDto updateDto = UpdateCartRequestDto.builder().quantity(3).build();  // 기존 수량(2) -> 새 수량(3)

        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(menuCartRepository.findByMenuUuid(productId)).thenReturn(Optional.of(menuCart));

        // when
        cartService.updateCart(1L, updateDto, productId);

        // then
        assertThat(menuCart.getQuantity()).isEqualTo(3);
        assertThat(menuCart.getTotalPrice()).isEqualTo(30000);
        verify(menuCartRepository, times(1)).save(menuCart);
    }

    @Test
    void 장바구니_업데이트_수량_0_삭제() {
        // given
        MenuCart menuCart = MenuCart.builder()
                .menu(menu)
                .cart(cart)
                .quantity(2)
                .totalPrice(20000)
                .build();
        cart.getMenuCarts().add(menuCart);

        UUID productId = menuCart.getMenu().getMenuUuId();
        UpdateCartRequestDto updateDto = UpdateCartRequestDto.builder().quantity(0).build();  // 수량 0이면 삭제

        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(menuCartRepository.findByMenuUuid(productId)).thenReturn(Optional.of(menuCart));

        // when
        cartService.updateCart(1L, updateDto, productId);

        // then
        verify(menuCartRepository, times(1)).delete(menuCart);
    }

    @Test
    void 존재하지_않는_장바구니_조회_예외발생() {
        // given
        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cartService.searchCart(1L))
                .isInstanceOf(CustomCartException.class);
    }

    @Test
    void 존재하지_않는_메뉴_업데이트_예외발생() {
        // given
        UUID productId = UUID.randomUUID();
        UpdateCartRequestDto updateDto = UpdateCartRequestDto.builder().quantity(3).build();

        when(userHelper.getUserById(1L)).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(menuCartRepository.findByMenuUuid(productId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cartService.updateCart(1L, updateDto, productId))
                .isInstanceOf(CustomMenuException.class);
    }
}