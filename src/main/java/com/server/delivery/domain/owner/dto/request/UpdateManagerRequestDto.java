package com.server.delivery.domain.owner.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateManagerRequestDto {
    private String managerId;
    private String storeId;
}
