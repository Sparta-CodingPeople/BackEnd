package com.server.delivery.domain.cart.controller;

import com.server.delivery.domain.cart.dto.CartItemDto;
import com.server.delivery.domain.cart.dto.request.CreateCartRequestDto;
import com.server.delivery.domain.cart.dto.request.UpdateCartRequestDto;
import com.server.delivery.domain.cart.dto.response.SearchCartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
public class CartController {


    //장바구니 등록
    @PostMapping
    public ResponseEntity<?> createCart(@RequestBody CreateCartRequestDto createCartRequestDto)
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "상품이 장바구니에 추가되었습니다");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }

    //장바구니 조회
    @GetMapping
    public ResponseEntity<SearchCartResponseDto> SearchCart(){

        CartItemDto item1 = CartItemDto.builder()
                .productId(UUID.fromString("00000000-0000-0000-0000-000000000123"))
                .productName("김치찌개")
                .quantity(3)
                .price(10000)
                .build();
        CartItemDto item2= CartItemDto.builder()
                .productId(UUID.fromString("00000000-0000-0000-0000-000000000124"))
                .productName("된장찌개")
                .quantity(2)
                .price(11000)
                .build();
        List<CartItemDto> dummyItems = new ArrayList<>();
        dummyItems.add(item1);
        dummyItems.add(item2);

        SearchCartResponseDto dummyResponse = SearchCartResponseDto.builder()
                .cartId(UUID.fromString("00000000-0000-0000-0000-000000000123"))
                .items(dummyItems)
                .totalPrice(3500)
                .build();
        return ResponseEntity.ok(dummyResponse);

    }

    //장바구니 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCart(){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "상품이 장바구니에서 삭제되었습니다");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }

    //장바구니 수정
    @PatchMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> updateCart(
            @RequestBody UpdateCartRequestDto updateCartRequestDto,
            @PathVariable UUID productId
    ){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "장바구니 상품 수량이 수정되었습니다");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }

}
