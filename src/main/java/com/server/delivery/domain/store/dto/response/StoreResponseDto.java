package com.server.delivery.domain.store.dto.response;

import lombok.*;

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
    private int storeLocationId;
    private long storeOperationId;
    private String phoneNumber;
    private boolean storeIsDeleted;
    private boolean storeIsGranted;



}
