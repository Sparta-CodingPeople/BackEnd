package com.server.delivery.domain.delivery.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.delivery.dto.req.DeliveryUpdateRequestDto;
import com.server.delivery.domain.delivery.dto.res.DeliverySearchResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryUpdateResponseDto;
import com.server.delivery.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    // 배달 정보 조회
    @GetMapping("/v1/deliveries/{deliveryId}")
    public ResponseEntity<DeliverySearchResponseDto> searchDeliveryInfo(
            @PathVariable String deliveryId,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        DeliverySearchResponseDto response = deliveryService.searchDeliveryInfo(userDetail, deliveryId);
        return ResponseEntity.ok(response);
    }

    // 배달 상태 변경 (관리자, 사장)
    @PatchMapping("/v1/deliveries/{deliveryId}/status")
    public ResponseEntity<DeliveryUpdateResponseDto> updateDeliveryStatus(
            @PathVariable String deliveryId,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            DeliveryUpdateRequestDto request
    ) {
        DeliveryUpdateResponseDto response = deliveryService.updateDeliveryStatus(deliveryId, userDetail, request);
        return ResponseEntity.ok(response);
    }
}
