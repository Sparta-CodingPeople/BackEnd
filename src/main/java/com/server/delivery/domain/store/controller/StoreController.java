package com.server.delivery.domain.store.controller;

import com.server.delivery.common.PageCustom;
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
    public ResponseEntity<StoreResponseDto> registerStore(@RequestBody StoreRequestDto requestDto) {

            storeService.registerStore(requestDto);
            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 등록 요청 성공, 관리자의 승인이 필요합니다.")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);

    }

    // 매장 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<StoreResponseDto> deleteStore(
            @PathVariable UUID id,
            @AuthenticationPrincipal String password ) {

            storeService.deleteStore(id, password);

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 삭제 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
    }

    // 매장 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable UUID id,
            @RequestBody StoreRequestDto requestDto) {
            storeService.updateStore(id, requestDto);

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 정보 수정 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);

    }

    // 매장 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable UUID id) {

        StoreResponseDto storeData = storeService.getStore(id);

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 단일 조회 성공")
                    .data(storeData)
                    .build();

            return ResponseEntity.ok(response);

    }

    // 매장 위치 수정
    @PatchMapping("/{id}/location")
    public ResponseEntity<StoreResponseDto> updateStoreLocation(
            @PathVariable UUID id,
            @RequestBody StoreLocationRequestDto requestDto) {

             storeService.updateStoreLocation(id, requestDto);


            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 위치 수정 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);

    }

    // 매장 영업 시간 수정
    @PatchMapping("/{id}/time")
    public ResponseEntity<StoreResponseDto> updateStoreOperatingHours(
            @PathVariable UUID id,
            @RequestBody StoreOperatingHoursRequestDto[] requestDto) {

        storeService.updateStoreOperatingHours(id, requestDto);


            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 운영 시간 수정 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);

    }

    // 매장 검색
    @GetMapping
    public ResponseEntity<StoreResponseDto> searchStores(
            @RequestParam("search") String search,
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sort") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        List<StoreResponseDto> storeList = storeService.searchStores(search, pageable);

        Long totalStores = storeService.getTotalStores(search);

        PageCustom<StoreResponseDto> pageResultList = new PageCustom<>(storeList, pageable, totalStores);

        StoreResponseDto response = StoreResponseDto.builder()
                .status("200")
                .message("매장 검색 결과")
                .data(pageResultList)
                .build();

        return ResponseEntity.ok(response);
    }
}
