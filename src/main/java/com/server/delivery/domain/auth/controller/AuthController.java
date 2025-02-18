package com.server.delivery.domain.auth.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.auth.dto.request.CustomerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.OwnerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.SignInRequestDto;
import com.server.delivery.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 고객 회원가입
    @PostMapping(value = "/api/v1/auth/sign-up/customer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomResponse<Void> createCustomerAccount(
            @RequestPart CustomerCreateRequestDto customerCreateRequestDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        authService.createCustomerAccount(customerCreateRequestDto, profileImage);

        return CustomResponse.success("회원가입 성공");  // 회원가입 후 생성된 JWT 토큰 반환
    }

    //사업주 회원가입
    @PostMapping(value = "/api/v1/auth/sign-up/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CustomResponse<Void> createOwnerAccount(
            @RequestPart OwnerCreateRequestDto ownerCreateRequestDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        authService.createOwnerAccount(ownerCreateRequestDto, profileImage);
        return CustomResponse.success("사업자 회원가입 성공");  // 회원가입 후 생성된 JWT 토큰 반환
    }

    //로그인
    @PostMapping(value = "/api/v1/auth/sign-in")
    public CustomResponse<Void> signIn(
            @RequestBody SignInRequestDto signInRequestDto,
            HttpServletResponse response
    ) {
        String token = authService.signInCustomer(signInRequestDto);
        response.setHeader("Authorization", "Bearer " + token);

        return CustomResponse.success("로그인 성공");  // 회원가입 후 생성된 JWT 토큰 반환
    }

    //토큰 재발급
    @PostMapping(value = "/api/v1/auth/renew")
    public CustomResponse<Void> renewToken(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            HttpServletResponse response
    ) {
        String renewToken = authService.renewToken(customUserDetail);
        response.setHeader("Authorization", "Bearer " + renewToken);
        return CustomResponse.success("토큰 갱신 성공");  // 회원가입 후 생성된 JWT 토큰 반환
    }
}