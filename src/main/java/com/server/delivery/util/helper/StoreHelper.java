package com.server.delivery.util.helper;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StoreHelper {
    private final StoreRepository storeRepository;

    public Store getStoreByUuid(UUID storeUuid) {
        return storeRepository.findByStoreUuid(storeUuid).orElseThrow(
                () -> new CustomStoreException(ExceptionCode.STORE_NOT_FOUND)
        );
    }
}
