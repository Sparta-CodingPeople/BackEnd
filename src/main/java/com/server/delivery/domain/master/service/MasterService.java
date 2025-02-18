package com.server.delivery.domain.master.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Pageable;

public interface MasterService {
    PageCustom<StoreResponseDto> getStoreListNeedGrant(Long id, Pageable pageable);

    void approveStore(Long userId, String storeId);
}
