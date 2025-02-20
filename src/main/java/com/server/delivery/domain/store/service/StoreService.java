package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.*;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    void registerStore(StoreRegisterRequestDto requestDto);

    void deleteStore(UUID id, String userPassword, String userName);

    void updateStore(UUID id, String userName, StoreUpdateRequestDto requestDto);

    StoreResponseDto getStore(UUID id);

    void updateStoreLocation(UUID id, StoreLocationUpdateDto requestDto);

    void updateStoreOperatingHours(UUID id, List<StoreOperatingHoursUpdateDto> requestDto);

    List<StoreResponseDto> searchStores(String search, Pageable pageable);

    Long getTotalStores(String search);
}
