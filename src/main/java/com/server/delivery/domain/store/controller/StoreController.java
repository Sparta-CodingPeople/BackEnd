package com.server.delivery.domain.store.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.store.dto.request.*;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CustomResponse<Void>> registerStore(@RequestBody StoreRegisterRequestDto requestDto) {

            storeService.registerStore(requestDto);

            return ResponseEntity.ok(CustomResponse.success("매장 등록 요청 성공"));
    }

    // 매장 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> deleteStore(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetail userDetail ) {

            // userDetail이 null일 경우 임시 비밀번호 설정(테스트용)
            String userPassword = (userDetail != null) ? userDetail.getPassword() : "password1234!";

            storeService.deleteStore(id, userPassword);

            return ResponseEntity.ok(CustomResponse.success("매장 삭제 성공"));
    }

    // 매장 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> updateStore(
            @PathVariable UUID id,
            @RequestBody StoreUpdateRequestDto requestDto) {

        storeService.updateStore(id, requestDto);

        return ResponseEntity.ok(CustomResponse.success("매장 정보 수정 성공"));

    }

    // 매장 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<StoreResponseDto>> getStore(@PathVariable UUID id) {

        StoreResponseDto storeData = storeService.getStore(id);

        return ResponseEntity.ok(CustomResponse.success("매장 단일 조회 성공", storeData));

    }

    // 매장 위치 수정
    @PatchMapping("/{id}/location")
    public ResponseEntity<CustomResponse<Void>> updateStoreLocation(
            @PathVariable UUID id,
            @RequestBody StoreLocationRequestDto requestDto) {

             storeService.updateStoreLocation(id, requestDto);

             return ResponseEntity.ok(CustomResponse.success("매장 위치 수정 성공"));

    }

    // 매장 영업 시간 수정
    @PatchMapping("/{id}/time")
    public ResponseEntity<CustomResponse<Void>> updateStoreOperatingHours(
            @PathVariable UUID id,
            @RequestBody List<StoreOperatingHoursRequestDto> requestDto) {

        storeService.updateStoreOperatingHours(id, requestDto);

        return ResponseEntity.ok(CustomResponse.success("매장 운영 시간 수정 성공"));

    }

    // 매장 검색
    @GetMapping
    public ResponseEntity<CustomResponse<PageCustom<StoreResponseDto>>> searchStores(
            @RequestParam("search") String search,
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sort") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        List<StoreResponseDto> storeList = storeService.searchStores(search, pageable);

        Long totalStores = storeService.getTotalStores(search);

        PageCustom<StoreResponseDto> pageResultList = new PageCustom<>(storeList, pageable, totalStores);

        return ResponseEntity.ok(CustomResponse.success("매장 검색 결과", pageResultList));
    }
}
