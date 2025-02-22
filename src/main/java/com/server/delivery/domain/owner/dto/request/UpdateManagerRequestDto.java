package com.server.delivery.domain.owner.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateManagerRequestDto {
    private UUID storeUuid;
}
