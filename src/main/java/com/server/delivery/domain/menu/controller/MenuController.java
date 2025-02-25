package com.server.delivery.domain.menu.controller;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.common.response.CustomResponse;
import com.server.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.server.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import com.server.delivery.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @PostMapping("/{storeUuid}")
    public CustomResponse<Void> registerMenu(
            @PathVariable UUID storeUuid,
            @RequestPart("menuData") MenuCreateRequestDto requestDto,
            @RequestPart(value = "foodImage", required = false) MultipartFile foodImage,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        menuService.registerMenu(customUserDetail.getUserId(), storeUuid, requestDto, foodImage);

        return CustomResponse.success("메뉴 등록 성공");
    }

    // 메뉴 수정
    @PutMapping("/{menuUuid}/menu")
    public CustomResponse<Void> updateMenu(
            @PathVariable UUID menuUuid,
            @RequestPart(value = "requestDto") MenuUpdateRequestDto requestDto,
            @RequestPart(value = "foodImage") MultipartFile foodImage,
            @AuthenticationPrincipal CustomUserDetail userDetail
    ) {

        menuService.updateMenu(userDetail.getUserId(), menuUuid, requestDto, foodImage);

        return CustomResponse.success("메뉴 수정 성공");
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuUuid}/menu")
    public CustomResponse<Void> deleteMenu(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable UUID menuUuid) {

        menuService.deleteMenu(customUserDetail.getUserId(), menuUuid);

        return CustomResponse.success("메뉴 삭제 성공");
    }

    // 메뉴 단일 조회
    @GetMapping("/{menuUuid}/menu")
    public CustomResponse<MenuResponseDto> getMenu(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable UUID menuUuid) {

        MenuResponseDto response = menuService.getMenu(customUserDetail.getUserId(), menuUuid);

        return CustomResponse.success("메뉴 조회 성공", response);
    }

    // 메뉴 검색
    @GetMapping("/{storeUuid}/store")
    public CustomResponse<PageCustom<MenuResponseDto>> searchMenus(
            @PathVariable UUID storeUuid,
            @RequestParam("search") String search,
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        PageCustom<MenuResponseDto> resultList = menuService.searchMenus(customUserDetail.getUserId(), storeUuid, search, pageable);

        return CustomResponse.success("메뉴 검색 결과", resultList);
    }

}
