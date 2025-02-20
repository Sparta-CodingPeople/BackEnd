package com.server.delivery.domain.store.dto.response;

import com.server.delivery.model.store.entity.OperationTimes;
import com.server.delivery.model.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class StoreResponseDto {
    private String status;
    private String message;
    private Object data;

    private UUID storeUuid;
    private String storeName;
    private int storeCategoryId;
    private UUID storeLocationId;
    private String storeDescription;
    private String address;
    private String operationTimeOpeningTime;
    private String operationTimeClosingTime;
    private String phoneNumber;
    private boolean storeIsDeleted;
    private boolean storeIsGranted;

    public static StoreResponseDto fromEntity(Store store) {
        return StoreResponseDto.builder()
                .storeUuid(store.getStoreUuid())
                .storeName(store.getStoreName())
                .phoneNumber(store.getPhoneNumber())
                .storeDescription(store.getStoreDescription())
                .storeIsDeleted(store.isStoreIsDeleted())
                .storeIsGranted(store.isStoreIsGranted())
                .address(store.getStoreLocation().getLocation().getAddress())
                .build();
    }


}
