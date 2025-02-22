package com.server.delivery.domain.order.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.common.response.ResponseMessage;
import com.server.delivery.domain.order.dto.request.*;
import com.server.delivery.domain.order.dto.response.OrderGetResponseDto;
import com.server.delivery.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    //주문 등록하게되면 주문 조회시 아래 주문이 보여야함 (주문 등록에서 주문 아이디를 반환하지 않고 리스트에서 다시 조회하여 클릭)
    @PostMapping
    public CustomResponse<Void> createOrder(
            @RequestBody OrderCreateRequestDto orderCreateRequestDto,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        orderService.createOrder(userDetail.getUserId(), orderCreateRequestDto);
        return CustomResponse.success(ResponseMessage.ORDER_REQUEST.getMessage());
    }

    //주문수정 -> 취소, 거절이 있기에 수정에서는 메뉴 개수에 대한 수정 또는 메뉴 추가하는 로직이 들어가야 한다.
    //Master만 가능
    @PatchMapping("/{orderUuid}")
    public CustomResponse<Void> updateOrder(
            @PathVariable UUID orderUuid,
            @RequestBody OrderUpdateRequestDto orderUpdateRequestDto
    ) {
        orderService.updateOrder(orderUuid, orderUpdateRequestDto);

        return CustomResponse.success("주문이 수정되었습니다.");
    }

    //주문조회
    @GetMapping("/{orderUuid}")
    public CustomResponse<OrderGetResponseDto> getOrder(@PathVariable UUID orderUuid) {
        OrderGetResponseDto response = orderService.findOrder(orderUuid);

        return CustomResponse.success("주문이 조회되었습니다.", response);
    }

    //주문취소
    @PatchMapping("/{orderId}/cancel")
    public CustomResponse<Void> deleteOrder(
            @RequestBody DeleteOrderReqeustDto deleteOrderReqeustDto,
            @PathVariable UUID orderId
    ) {
        orderService.deleteOrder(orderId);

        return CustomResponse.success("주문이 취소되었습니다.");
    }

    //주문접수
    @PostMapping("/{orderId}/accept")
    public CustomResponse<Void> acceptOrder(
            @RequestBody OrderAcceptRequestDto orderAcceptRequestDto,
            @PathVariable UUID orderId
    ) {
        orderService.acceptOrder(orderId, orderAcceptRequestDto);

        return CustomResponse.success("주문 접수되었습니다");
    }

    //주문거부
    @PostMapping("/{orderId}/reject")
    public CustomResponse<Void> rejectOrder(
            @RequestBody OrderRejectRequestDto orderRejectRequestDto,
            @PathVariable UUID orderId
    ) {
        orderService.rejectOrder(orderId, orderRejectRequestDto);

        return CustomResponse.success("주문이 거부되었습니다");
    }

}
