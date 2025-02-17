package com.server.delivery.domain.store.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class StoreResponseDto {
    private String status;
    private String message;
    private Object data;

    private UUID storeId;
    private String storeName;
    private UUID ownerUuid;
    private UUID managerUuid;
    private int storeCategoryId;
    private int storeLocationId;
    private long storeOperationId;
    private String contactNumber;
    private boolean isDeleted;
    private boolean isGranted;



}
