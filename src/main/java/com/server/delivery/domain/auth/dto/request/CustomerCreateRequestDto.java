package com.server.delivery.domain.auth.dto.request;

import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.entity.constant.UserGender;
import com.server.delivery.model.user.entity.constant.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateRequestDto {

    @NotBlank(message = "Username은 필수 입력값입니다.")
    @Size(min = 4, max = 10, message = "Username은 최소 4자 이상, 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username은 알파벳 소문자(a~z)와 숫자(0~9)로만 구성될 수 있습니다.")
    private String username;

    @NotBlank(message = "Password는 필수 입력값입니다.")
    @Size(min = 8, max = 15, message = "Password는 최소 8자 이상, 15자 이하이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password는 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "First name은 필수 입력값입니다.")
    @Size(min = 1, max = 10)
    private String firstName;

    @NotBlank(message = "Last name은 필수 입력값입니다.")
    @Size(min = 1, max = 10)
    private String lastName;

    @NotBlank(message = "Nickname은 필수 입력값입니다.")
    @Size(min = 1, max = 10)
    private String nickname;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "Phone number는 000-0000-0000 형식이어야 합니다.")
    private String phoneNumber;

    private String profileImage;

    @NotNull(message = "Gender는 필수 입력값입니다.")
    private Integer gender;

    @NotBlank(message = "Birth date는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Birth date는 YYYY-MM-DD 형식이어야 합니다.")
    private String birthDate;

    // User 엔티티로 변환하는 메서드
    public static User from(CustomerCreateRequestDto dto, String profileImage) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .profileImage(profileImage)
                .userRole(UserRole.CUSTOMER)
                .gender(UserGender.fromNumber(dto.getGender()))
                .birthDate(LocalDate.parse(dto.getBirthDate()))
                .build();
    }
}