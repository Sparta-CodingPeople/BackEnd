package com.server.delivery.common.jpa;

import com.server.delivery.common.jwt.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return Optional.empty();
        }
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        return Optional.of(userDetail.getUsername());  // 또는 userDetail.getUuid()를 반환할 수도 있습니다.
    }
}
