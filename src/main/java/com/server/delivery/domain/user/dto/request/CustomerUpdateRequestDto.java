package com.server.delivery.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerUpdateRequestDto {

    @NotBlank(message = "Nickname은 필수 입력값입니다.")
    @Size(min = 1, max = 10)
    private String nickname;

    private boolean isPublic;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "Phone number는 000-0000-0000 형식이어야 합니다.")
    private String phoneNumber;
}
