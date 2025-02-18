package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.*;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StoreService {

    public int deleteStore(
            UUID id,
            String password) {
        return 0;
    }

    public int updateStore(
            UUID id,
            StoreUpdateRequestDto requestDto) {
        return 0;
    }

    public StoreResponseDto getStore(UUID id) {

        return null;
    }

    public int updateStoreLocation(
            UUID id,
            StoreLocationRequestDto requestDto) {
        return 0;
    }

    public int updateStoreOperatingHours(
            UUID id,
            List<StoreOperatingHoursRequestDto> requestDto) {
        return 0;
    }

    public List<StoreResponseDto> searchStores(
            String search,
            Pageable pageable) {
        return null;
    }

    public Long getTotalStores(String search) {
        return null;
    }

    public int registerStore(StoreRegisterRequestDto requestDto) {
        return 0;
    }
}
