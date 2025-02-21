package com.server.delivery.domain.owner.service;

import com.server.delivery.domain.owner.dto.request.UpdateManagerRequestDto;

public interface OwnerService {
    void setManager(Long toBeManagerUserId, UpdateManagerRequestDto updateManagerRequestDto, Long ownerUserId);

    void updateManager(Long changedUser, UpdateManagerRequestDto updateManagerRequestDto, Long userId);
}
