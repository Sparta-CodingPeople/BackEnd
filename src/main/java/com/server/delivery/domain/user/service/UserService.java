package com.server.delivery.domain.user.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.user.dto.request.CustomerUpdateRequestDto;
import com.server.delivery.domain.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void updateUser(CustomerUpdateRequestDto customerUpdateRequestDto, MultipartFile profileImage, CustomUserDetail customUserDetail);

    UserResponseDto findUserById(Long userId, CustomUserDetail customUserDetail);

    PageCustom<UserResponseDto> searchUser(CustomUserDetail customUserDetail, String keyword, Pageable pageable);

    ResponseEntity<Void> deleteUser(CustomUserDetail customUserDetail);
}
