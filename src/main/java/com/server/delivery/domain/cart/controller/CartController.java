package com.server.delivery.domain.cart.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.cart.dto.request.CreateCartRequestDto;
import com.server.delivery.domain.cart.dto.request.UpdateCartRequestDto;
import com.server.delivery.domain.cart.dto.response.SearchCartResponseDto;
import com.server.delivery.domain.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {
    private final CartService cartService;

    //장바구니 등록
    @PostMapping
    public CustomResponse<Void> createCart(
            @RequestBody CreateCartRequestDto createCartRequestDto,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        cartService.createCart(userDetail.getUserId(), createCartRequestDto);

        return CustomResponse.success("상품이 장바구니에 추가되었습니다.");
    }

    //장바구니 조회
    @GetMapping
    public CustomResponse<SearchCartResponseDto> searchCart(
            @AuthenticationPrincipal CustomUserDetail userDetail) {

        SearchCartResponseDto cartResponseDtos = cartService.searchCart(userDetail.getUserId());

        return CustomResponse.success("장바구니 조회 성공", cartResponseDtos);

    }

    //장바구니 삭제
    @DeleteMapping
    public CustomResponse<Void> deleteCart(
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        cartService.deleteCart(userDetail.getUserId());

        return CustomResponse.success("상품이 장바구니에서 삭제되었습니다.");
    }

    //장바구니 수정
    @PatchMapping("/{productId}")
    public CustomResponse<Void> updateCart(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody UpdateCartRequestDto updateCartRequestDto,
            @PathVariable UUID productId
    ) {
        cartService.updateCart(userDetail.getUserId(), updateCartRequestDto, productId);
        return CustomResponse.success("장바구니 수정이 완료되었습니다.");
    }

}
