package com.server.delivery.domain.order.controller;

import com.server.delivery.domain.order.dto.OrderItemDto;
import com.server.delivery.domain.order.dto.request.OrderAcceptRequestDto;
import com.server.delivery.domain.order.dto.request.OrderCreateRequestDto;
import com.server.delivery.domain.order.dto.request.OrderRejectRequestDto;
import com.server.delivery.domain.order.dto.response.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class OrderController {
//    private final OrderService orderService;

    //주문 등록
    @PostMapping("/v1/orders")
    public ResponseEntity<?> createOrder(
            @RequestBody OrderCreateRequestDto orderCreateRequestDto
    )
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "주문 완료 되었습니다!");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }
    //주문수정
    @PatchMapping("/v1/orders/{orderId}")
    public ResponseEntity<?> updateOrder(
//            @RequestBody
            @PathVariable UUID orderId
            )
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "주문 수정 되었습니다!");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }
    //주문조회
    @GetMapping("v1/orders/{orderId}")
    public ResponseEntity<?> checkOrder(
            @PathVariable UUID orderId
    )
    {
        //더미데이터
        OrderItemDto item1 = OrderItemDto.builder()
                .productId(UUID.fromString("00000000-0000-0000-0000-000000000123"))
                .productCount(2)
                .productName("김치찌개")
                .productPrice(10000)
                .build();
        OrderItemDto item2 = OrderItemDto.builder()
                .productId(UUID.fromString("00000000-0000-0000-0000-000000000124"))
                .productCount(2)
                .productName("콜라1L")
                .productPrice(5000)
                .build();
        List<OrderItemDto> dummyItems = new ArrayList<>();
        dummyItems.add(item1);
        dummyItems.add(item2);

        OrderResponseDto dummyResponse = OrderResponseDto.builder()
                .userId(123L)
                .userName("DummyUser")
                .storeId(UUID.fromString("00000000-0000-0000-0000-100000000123"))
                .storeName("dummyStore")
                .items(dummyItems)
                .deliveryTip(3000)
                .totalprice(13000)
                .deliveryAddress("서울 종로구 광화문 XX")
                .messageForRider("문앞에 놓아주세요")
                .messageForStore("반찬 빼주세요")
                .orderType("온라인")
                .payType("신용카드")
                .orderTime(LocalDateTime.now())
                .userPhoneNum("000-000-0000")
                .deliveryStatus("배달중")
                .payStatus("결제완료")
                .build();
        return ResponseEntity.ok(dummyResponse);
    }

    //주문취소
    @PostMapping("v1/orders/{orderId}/cancel")
    public ResponseEntity<?> deleteOrder(
            @PathVariable UUID orderId
    )
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "주문이 취소되었습니다");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }

    //주문접수
    @PostMapping("v1/orders/{orderId}/accept")
    public ResponseEntity<?> acceptOrder(
            @RequestBody OrderAcceptRequestDto orderAcceptRequestDto,
            @PathVariable UUID orderId
    )
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "주문 접수했습니다!");
        responseBody.put("data", null);
        return ResponseEntity.ok(responseBody);
    }

    //주문거부
    @PostMapping("v1/orders/{orderId}/reject")
    public ResponseEntity<?> rejectOrder(
            @RequestBody OrderRejectRequestDto orderRejectRequestDto,
            @PathVariable UUID orderId
    )
    {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", 200);
        responseBody.put("message", "주문 거절했습니다!");

        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> item = new HashMap<>();
        item.put("message", "재료소진으로 주문 취소되었습니다.");
        dataList.add(item);

        responseBody.put("data", dataList);

        return ResponseEntity.ok(responseBody);
    }



}
