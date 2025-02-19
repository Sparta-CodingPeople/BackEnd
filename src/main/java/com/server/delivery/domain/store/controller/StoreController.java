package com.server.delivery.domain.store.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.store.dto.request.StoreLocationRequestDto;
import com.server.delivery.domain.store.dto.request.StoreOperatingHoursRequestDto;
import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;
import com.server.delivery.domain.store.dto.request.StoreUpdateRequestDto;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    // 매장 등록
    @PostMapping
    public CustomResponse<Void> registerStore(@RequestBody StoreRegisterRequestDto requestDto) {

        storeService.registerStore(requestDto);

        return CustomResponse.success("매장 등록 요청 성공");
    }

    // 매장 삭제
    @DeleteMapping("/{id}")
    public CustomResponse<Void> deleteStore(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetail userDetail) {

        // userDetail이 null일 경우 임시 비밀번호 설정(테스트용)
        String userPassword = (userDetail != null) ? userDetail.getPassword() : "password1234!";

        storeService.deleteStore(id, userPassword);

        return CustomResponse.success("매장 삭제 성공");
    }

    // 매장 정보 수정
    @PatchMapping("/{id}")
    public CustomResponse<Void> updateStore(
            @PathVariable UUID id,
            @RequestBody StoreUpdateRequestDto requestDto) {

        storeService.updateStore(id, requestDto);

        return CustomResponse.success("매장 정보 수정 성공");

    }

    // 매장 단일 조회
    @GetMapping("/{id}")
    public CustomResponse<StoreResponseDto> getStore(@PathVariable UUID id) {

        StoreResponseDto storeData = storeService.getStore(id);

        return CustomResponse.success("매장 단일 조회 성공", storeData);

    }

    // 매장 위치 수정
    @PatchMapping("/{id}/location")
    public CustomResponse<Void> updateStoreLocation(
            @PathVariable UUID id,
            @RequestBody StoreLocationRequestDto requestDto) {

        storeService.updateStoreLocation(id, requestDto);

        return CustomResponse.success("매장 위치 수정 성공");

    }

    // 매장 영업 시간 수정
    @PatchMapping("/{id}/time")
    public CustomResponse<Void> updateStoreOperatingHours(
            @PathVariable UUID id,
            @RequestBody List<StoreOperatingHoursRequestDto> requestDto) {

        storeService.updateStoreOperatingHours(id, requestDto);

        return CustomResponse.success("매장 운영 시간 수정 성공");

    }

    // 매장 검색
    @GetMapping
    public CustomResponse<PageCustom<StoreResponseDto>> searchStores(
            @RequestParam("search") String search,
            @PageableDefault Pageable pageable
    ) {

        List<StoreResponseDto> storeList = storeService.searchStores(search, pageable);

        Long totalStores = storeService.getTotalStores(search);

        PageCustom<StoreResponseDto> pageResultList = new PageCustom<>(storeList, pageable, totalStores);

        return CustomResponse.success("매장 검색 결과", pageResultList);
    }
}
