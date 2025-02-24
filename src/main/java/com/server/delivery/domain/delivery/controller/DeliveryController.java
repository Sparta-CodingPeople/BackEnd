package com.server.delivery.domain.delivery.controller;

import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.common.response.ResponseMessage;
import com.server.delivery.domain.delivery.dto.req.DeliveryCompleteRequestDto;
import com.server.delivery.domain.delivery.dto.req.DeliveryStartRequestDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryCompleteResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliverySearchResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryStartResponseDto;
import com.server.delivery.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    // 배달 시작 (사장님)
    @PatchMapping("/v1/deliveries/{deliveryId}/start")
    public CustomResponse<DeliveryStartResponseDto> startDelivery(
            @PathVariable UUID deliveryId,
            @RequestBody DeliveryStartRequestDto request
    ) {
        DeliveryStartResponseDto response = deliveryService.startDelivery(deliveryId, request);
        return CustomResponse.success(ResponseMessage.DELIVERY_START.getMessage(), response);
    }

    // 배달 완료 (라이더)
    @PatchMapping("/v1/deliveries/{deliveryId}/complete")
    public CustomResponse<DeliveryCompleteResponseDto> completeDelivery(
            @PathVariable UUID deliveryId,
            @RequestBody DeliveryCompleteRequestDto request
    ) {
        DeliveryCompleteResponseDto response = deliveryService.completeDelivery(deliveryId, request);
        return CustomResponse.success(ResponseMessage.DELIVERY_COMPLETE.getMessage(), response);
    }

    // 배달 조회
    @GetMapping("/v1/deliveries/{deliveryId}")
    public CustomResponse<DeliverySearchResponseDto> searchDelivery(@PathVariable UUID deliveryId) {
        DeliverySearchResponseDto response = deliveryService.searchDelivery(deliveryId);
        return CustomResponse.success(ResponseMessage.DELIVERY_SEARCH.getMessage(), response);
    }

    // 배달 목록 조회
    @GetMapping("/v1/deliveries")
    public CustomResponse<PagedModel<DeliverySearchResponseDto>> searchDeliveries(
            @RequestParam(required = false) String keyword,
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC, page = 0, size = 10) Pageable pageable
    ) {
        PagedModel<DeliverySearchResponseDto> response = deliveryService.searchDeliveries(keyword, pageable);
        return CustomResponse.success(ResponseMessage.DELIVERY_SEARCH_ALL.getMessage(), response);
    }
}
