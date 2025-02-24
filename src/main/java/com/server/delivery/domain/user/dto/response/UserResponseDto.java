package com.server.delivery.domain.user.dto.response;

import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.entity.constant.UserGender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String username;
    private String nickname;
    private String phoneNumber;
    private UserGender gender;
    private LocalDate birthDate;
    private boolean isPublic;
    private String profileIamge;

    public static UserResponseDto from(User user) {
        if (!user.getIsPublic()) {
            UserResponseDto.builder()
                    .id(user.getId())
                    .name(user.getFirstName() + " " + user.getLastName())
                    .username(null)
                    .phoneNumber(null)
                    .gender(null)
                    .birthDate(null)
                    .isPublic(user.getIsPublic())
                    .profileIamge(user.getProfileImage())
                    .build();
        }
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getFirstName() + " " + user.getLastName())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .isPublic(user.getIsPublic())
                .profileIamge(user.getProfileImage())
                .nickname(user.getNickname())
                .build();
    }
}
