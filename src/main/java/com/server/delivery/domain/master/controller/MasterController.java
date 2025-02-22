package com.server.delivery.domain.master.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.auth.service.AuthService;
import com.server.delivery.domain.master.dto.request.MasterSignInRequestDto;
import com.server.delivery.domain.master.service.MasterService;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master/v1")
public class MasterController {
    private final AuthService authService;
    private final MasterService masterService;

    @PostMapping("sign-in")
    public CustomResponse<Void> signIn(
            @RequestBody MasterSignInRequestDto masterSignInRequestDto,
            HttpServletResponse response
    ) {
        String accessToken = authService.signInMaster(masterSignInRequestDto);

        response.setHeader("Authorization", "Bearer " + accessToken);

        return CustomResponse.success("마스터 로그인 성공");
    }

    @GetMapping("/stores")
    public CustomResponse<PageCustom<StoreResponseDto>> getNeededGrantedStore(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PageableDefault Pageable pageable
    ) {
        PageCustom<StoreResponseDto> storeList = masterService.getStoreListNeedGrant(customUserDetail.getUserId(), pageable);


        return CustomResponse.success("입점 요청 매장 출력 성공", storeList);
    }

    @PostMapping("/stores/{storeUuid}/approve")
    public CustomResponse<Void> approveStore(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable("storeUuid") UUID storeUuid
    ) {
        masterService.approveStore(customUserDetail.getUserId(), storeUuid);

        return CustomResponse.success("입점 승인 성공");
    }

}
