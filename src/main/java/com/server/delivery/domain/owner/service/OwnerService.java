package com.server.delivery.domain.owner.service;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.owner.dto.request.UpdateManagerRequestDto;

public interface OwnerService {
    void updateManager(Long userId, UpdateManagerRequestDto updateManagerRequestDto, CustomUserDetail customUserDetail);
}
