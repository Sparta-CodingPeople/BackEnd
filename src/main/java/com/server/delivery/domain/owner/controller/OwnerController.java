package com.server.delivery.domain.owner.controller;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.owner.dto.request.UpdateManagerRequestDto;
import com.server.delivery.domain.owner.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @PutMapping("/{toBeManagerUserId}/toBeManagerUser")
    public ResponseEntity<Void> setManager(
            @PathVariable Long toBeManagerUserId,
            @RequestBody UpdateManagerRequestDto updateManagerRequestDto,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        ownerService.setManager(toBeManagerUserId, updateManagerRequestDto, customUserDetail.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{ChangedUser}/ChangedUser")
    public ResponseEntity<Void> updateManager(
            @PathVariable Long ChangedUser,
            @RequestBody UpdateManagerRequestDto updateManagerRequestDto,
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ) {
        ownerService.updateManager(ChangedUser, updateManagerRequestDto, customUserDetail.getUserId());
        return ResponseEntity.ok().build();
    }

}
