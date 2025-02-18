package com.server.delivery.domain.user.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.user.dto.request.CustomerUpdateRequestDto;
import com.server.delivery.domain.user.dto.response.UserResponseDto;
import com.server.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    //회원 정보 수정
    @PutMapping
    public CustomResponse<Object> updateUser(
            @RequestPart("customerUpdateRequestDto") CustomerUpdateRequestDto customerUpdateRequestDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        userService.updateUser(customerUpdateRequestDto, profileImage, customUserDetail);

        return CustomResponse.success("회원 정보 수정 성공", null);
    }

    //회원 정보 단일 조회
    @GetMapping("/{userId}")
    public CustomResponse<UserResponseDto> findUserById(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        UserResponseDto userResponseDto = userService.findUserById(userId, customUserDetail);

        return CustomResponse.success("조회 성공", userResponseDto);
    }

    //회원 정보 검색
    @GetMapping
    public CustomResponse<PageCustom<UserResponseDto>> searchUsers(
            @RequestParam("keyword") String keyword,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        PageCustom<UserResponseDto> userList = userService.searchUser(customUserDetail, keyword, pageable);
        return CustomResponse.success("회원 정보 조회 성공", userList);
    }

    //회원 탈퇴
    @DeleteMapping
    public CustomResponse<Void> deleteUser(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        userService.deleteUser(customUserDetail);

        return CustomResponse.success("회원 탈퇴 성공");
    }

}
