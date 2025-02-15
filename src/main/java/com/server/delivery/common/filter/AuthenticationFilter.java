package com.server.delivery.common.filter;

import com.server.delivery.common.exception.customException.CustomJwtException;
import com.server.delivery.common.jwt.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/sign-up", "/api/owner/v1/auth/sign-up", "/api/v1/auth/sign-in"
    );

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.요청 경로를 가져오기
        String requestURI = request.getRequestURI();

        //2. whileList 경로 등록 후 필터에서 제외되록 설정
        boolean isWhitelisted = EXCLUDE_URLS.stream().anyMatch(requestURI::matches);
        if (isWhitelisted) {
            filterChain.doFilter(request, response);
            return;
        }

        //3. Token 검증
        try {
            String accessToken = jwtHelper.resolveToken(request);
            if (accessToken != null && jwtHelper.validateToken(accessToken)) {
                Authentication authentication = jwtHelper.getAuthenticationFromAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }
        }catch (CustomJwtException e) {
            request.setAttribute("httpStatus",e.getHttpStatus());
            request.setAttribute("message", e.getMessage());
            request.setAttribute("code", e.getCode());
        }


        filterChain.doFilter(request, response);

    }
}
