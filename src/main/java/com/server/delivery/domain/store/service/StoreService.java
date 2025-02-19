package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.StoreLocationRequestDto;
import com.server.delivery.domain.store.dto.request.StoreOperatingHoursRequestDto;
import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;
import com.server.delivery.domain.store.dto.request.StoreUpdateRequestDto;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    void registerStore(StoreRegisterRequestDto requestDto);

    void deleteStore(UUID id, String userPassword);

    void updateStore(UUID id, StoreUpdateRequestDto requestDto);

    StoreResponseDto getStore(UUID id);

    void updateStoreLocation(UUID id, StoreLocationRequestDto requestDto);

    void updateStoreOperatingHours(UUID id, List<StoreOperatingHoursRequestDto> requestDto);

    List<StoreResponseDto> searchStores(String search, Pageable pageable);

    Long getTotalStores(String search);
}
