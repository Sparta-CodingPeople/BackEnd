package com.server.delivery.domain.user.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.user.dto.request.CustomerUpdateRequestDto;
import com.server.delivery.domain.user.dto.response.UserResponseDto;
import com.server.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> updateUser(
            @RequestPart("CustomerUpdateRequestDto") CustomerUpdateRequestDto customerUpdateRequestDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        userService.updateUser(customerUpdateRequestDto, profileImage, customUserDetail);

        return ResponseEntity.ok().build();
    }

    //회원 정보 단일 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUserById(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        UserResponseDto userResponseDto = userService.findUserById(userId, customUserDetail);

        return ResponseEntity.ok(userResponseDto);
    }

    //회원 정보 검색
    @GetMapping
    public ResponseEntity<PageCustom<UserResponseDto>> searchUsers(
            @RequestParam("keyword") String keyword,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        PageCustom<UserResponseDto> userList = userService.searchUser(customUserDetail, keyword, pageable);
        return ResponseEntity.ok().body(userList);
    }

    //회원 탈퇴
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        userService.deleteUser(customUserDetail);

        return ResponseEntity.ok().build();
    }

}
