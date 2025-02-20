package com.server.delivery.common.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

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
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails)principal;
			return Optional.of(userDetails.getUsername());
		}

		return Optional.empty();  // principal이 UserDetails가 아닐 경우
	}
}
