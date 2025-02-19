package com.server.delivery.domain.delivery.service;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.delivery.dto.req.DeliveryUpdateRequestDto;
import com.server.delivery.domain.delivery.dto.res.DeliverySearchResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryUpdateResponseDto;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    public DeliverySearchResponseDto searchDeliveryInfo(CustomUserDetail userDetail, String deliveryId) {
        return null;
    }

    public DeliveryUpdateResponseDto updateDeliveryStatus(
            String deliveryId,
            CustomUserDetail userDetail,
            DeliveryUpdateRequestDto request
    ) {
        return null;
    }
}
