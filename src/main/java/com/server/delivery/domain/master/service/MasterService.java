package com.server.delivery.domain.master.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MasterService {
    PageCustom<StoreResponseDto> getStoreListNeedGrant(Long id, Pageable pageable);

    void approveStore(Long userId, UUID storeUuid);
}
