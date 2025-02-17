package com.server.delivery.domain.store.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StoreRequestDto {
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
