package com.server.delivery.common.jpa;

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

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();  // 인증되지 않은 사용자일 경우
        }

        Object principal = authentication.getPrincipal();

        // principal이 UserDetails로 캐스팅될 수 있으면 username을 반환
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;
            return Optional.of(userDetails.getUsername());
        }

        return Optional.empty();  // principal이 UserDetails가 아닐 경우
    }
}
