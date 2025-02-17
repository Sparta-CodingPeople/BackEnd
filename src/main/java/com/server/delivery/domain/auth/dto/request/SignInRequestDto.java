package com.server.delivery.domain.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequestDto {
    private String username;
    private String password;
}
