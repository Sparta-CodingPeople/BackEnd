package com.server.delivery.domain.owner.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.owner.dto.request.UpdateManagerRequestDto;
import com.server.delivery.domain.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @PatchMapping
    public ResponseEntity<Void> updateManager(
            @RequestBody UpdateManagerRequestDto updateManagerRequestDto,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        return ResponseEntity.ok().build();
    }

}
