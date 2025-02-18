package com.server.delivery.domain.menu.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.server.delivery.domain.menu.service.MenuService;
import com.server.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @PostMapping("/{storeId}")
    public ResponseEntity<CustomResponse<Void>> registerMenu(
            @PathVariable UUID storeId,
            @RequestPart("menuData") MenuCreateRequestDto requestDto,
            @RequestPart(value = "foodImage", required = false) MultipartFile foodImage ) {

        menuService.registerMenu(storeId, requestDto, foodImage);


        return ResponseEntity.ok(CustomResponse.success("메뉴 등록 성공"));
    }

    // 메뉴 수정
    @PutMapping("/{storeId}/store/{menuId}/menu")
    public ResponseEntity<CustomResponse<Void>> updateMenu(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId,
            @RequestBody MenuUpdateRequestDto requestDto) {

        menuService.updateMenu(storeId, menuId, requestDto);
        
        return ResponseEntity.ok(CustomResponse.success("메뉴 수정 성공"));
    }

    // 메뉴 삭제
    @DeleteMapping("/{storeId}/store/{menuId}/menu")
    public ResponseEntity<CustomResponse<Void>> deleteMenu(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId) {

        menuService.deleteMenu(storeId, menuId);

        menuService.deleteMenu(storeId, menuId);

        return ResponseEntity.ok(CustomResponse.success("메뉴 삭제 성공"));
    }

    // 메뉴 단일 조회
    @GetMapping("/{storeId}/store/{menuId}/menu")
    public ResponseEntity<CustomResponse<MenuResponseDto>> getMenu(
            @PathVariable UUID storeId,
            @PathVariable UUID menuId) {

        MenuResponseDto response = menuService.getMenu(storeId, menuId);

        return ResponseEntity.ok(CustomResponse.success("메뉴 조회 성공", response));
    }

    // 메뉴 검색
    @GetMapping("/{storeId}/store")
    public ResponseEntity<CustomResponse<PageCustom<MenuResponseDto>>> searchMenus(
            @PathVariable UUID storeId,
            @RequestParam("search") String search,
            @RequestParam("size") int size,
            @RequestParam("page") int page,
            @RequestParam("sort") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        List<MenuResponseDto> resultList = menuService.searchMenus(storeId, search, pageable);
        Long totalMenus = menuService.getTotalMenus(storeId, search);

        PageCustom<MenuResponseDto> pageResultList = new PageCustom<>(resultList, pageable, totalMenus);

        return ResponseEntity.ok(CustomResponse.success("메뉴 검색 결과", pageResultList));
    }

}
