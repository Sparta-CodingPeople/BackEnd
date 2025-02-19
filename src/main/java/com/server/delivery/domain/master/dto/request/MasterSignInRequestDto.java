package com.server.delivery.domain.master.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MasterSignInRequestDto {
    private String username;
    private String password;
    private String masterCode;
}
