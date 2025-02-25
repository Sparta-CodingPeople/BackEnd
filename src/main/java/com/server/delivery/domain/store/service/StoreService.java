package com.server.delivery.domain.store.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.domain.store.dto.request.StoreLocationRequestDto;
import com.server.delivery.domain.store.dto.request.StoreOperatingHoursRequestDto;
import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;
import com.server.delivery.domain.store.dto.request.StoreUpdateRequestDto;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StoreService {

    void registerStore(Long userId, StoreRegisterRequestDto requestDto);

    void deleteStore(UUID id, String userPassword);

    void updateStore(Long userId, UUID storeUuid, StoreUpdateRequestDto requestDto);

    StoreResponseDto findStore(UUID storeUuid);

    void updateStoreLocation(UUID storeUuid, StoreLocationRequestDto requestDto);

    void updateStoreOperatingHours(UUID storeUuid, List<StoreOperatingHoursRequestDto> requestDto);

    PageCustom<StoreResponseDto> searchStores(String search, Pageable pageable);

    PageCustom<StoreResponseDto> searchStoresByArea(String search, Pageable pageable);
}
