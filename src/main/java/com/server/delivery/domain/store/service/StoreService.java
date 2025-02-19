package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;

public interface StoreService {
    void registerStore(StoreRegisterRequestDto requestDto);
}
