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

        int storeRegister =  storeService.registerStore(requestDto);

        if(storeRegister!=0) {

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 등록 요청 성공, 관리자의 승인이 필요합니다.")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);
        }else{

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("400")
                    .message("매장 등록 요청 실패!")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    // 매장 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<StoreResponseDto> deleteStore(
            @PathVariable UUID id,
            @AuthenticationPrincipal String password ) {


        int storeDelete = storeService.deleteStore(id, password);

        if(storeDelete !=0) {

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 삭제 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }else{

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("400")
                    .message("매장 삭제 실패!")
                    .data(null)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    // 매장 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable UUID id,
            @RequestBody StoreRequestDto requestDto) {
        int storeUpdate = storeService.updateStore(id, requestDto);

        if(storeUpdate!=0) {

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 정보 수정 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }else{

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("400")
                    .message("매장 정보 수정 실패!")
                    .data(null)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    // 매장 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable UUID id) {

        StoreResponseDto storeData = storeService.getStore(id);

        if(storeData!=null) {

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 단일 조회 성공")
                    .data(storeData)
                    .build();

            return ResponseEntity.ok(response);
        }else{

            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 단일 조회 실패!")
                    .data(storeData)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    // 매장 위치 수정
    @PatchMapping("/{id}/location")
    public ResponseEntity<StoreResponseDto> updateStoreLocation(
            @PathVariable UUID id,
            @RequestBody StoreLocationRequestDto requestDto) {

        int updateStoreLocation = storeService.updateStoreLocation(id, requestDto);

        if(updateStoreLocation!=0) {
            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 위치 수정 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }else{
            StoreResponseDto response = StoreResponseDto.builder()
                    .status("400")
                    .message("매장 위치 수정 실패!")
                    .data(null)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    // 매장 영업 시간 수정
    @PatchMapping("/{id}/time")
    public ResponseEntity<StoreResponseDto> updateStoreOperatingHours(
            @PathVariable UUID id,
            @RequestBody StoreOperatingHoursRequestDto[] requestDto) {

        int updateStoreOperatingHours = storeService.updateStoreOperatingHours(id, requestDto);

        if(updateStoreOperatingHours!=0) {
            StoreResponseDto response = StoreResponseDto.builder()
                    .status("200")
                    .message("매장 운영 시간 수정 성공")
                    .data(null)
                    .build();

            return ResponseEntity.ok(response);
        }else{
            StoreResponseDto response = StoreResponseDto.builder()
                    .status("400")
                    .message("매장 운영 시간 수정 실패")
                    .data(null)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }
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
