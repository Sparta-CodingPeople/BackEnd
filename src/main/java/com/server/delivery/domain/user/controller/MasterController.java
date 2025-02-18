package com.server.delivery.domain.user.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.auth.service.AuthService;
import com.server.delivery.domain.master.service.MasterService;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.domain.user.dto.request.MasterSignInRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master/v1")
public class MasterController {
    private final AuthService authService;
    private final MasterService masterService;

    @PostMapping("sign-in")
    public CustomResponse<Void> signIn(
            @RequestBody MasterSignInRequestDto masterSignInRequestDto
    ) {
        authService.signInMaster(masterSignInRequestDto);

        return CustomResponse.success("마스터 로그인 성공");
    }

    @PostMapping("/stores")
    public CustomResponse<PageCustom<StoreResponseDto>> getNeededGrantedStore(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PageableDefault Pageable pageable
    ) {
        PageCustom<StoreResponseDto> storeList = masterService.getStoreListNeedGrant(customUserDetail.getUserId(), pageable);


        return CustomResponse.success("입점 요청 매장 출력 성공", storeList);
    }

    @PostMapping("/stores/{storeId}/approve")
    public CustomResponse<Void> approveStore(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable("storeId") String storeId
    ) {
        masterService.approveStore(customUserDetail.getUserId(), storeId);

        return CustomResponse.success("입점 승인 성공");
    }

}
