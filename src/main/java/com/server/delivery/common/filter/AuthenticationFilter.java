package com.server.delivery.common.filter;

import com.server.delivery.common.exception.ExceptionCode;
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

    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/sign-up",
            "/api/owner/v1/auth/sign-up",
            "/api/v1/auth/sign-in",
            "/api/v1/auth/renew",
            "/api/v1/auth/sign-up/owner", // 새로 추가한 화이트리스트 URL
            "/api/v1/auth/sign-up/customer", // 새로 추가한 화이트리스트 URL
            "/api/v1/master/v1/sign-in" // 새로 추가한 화이트리스트 URL
    );
    private final JwtHelper jwtHelper;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.요청 경로를 가져오기
        String requestURI = request.getRequestURI();

        //2. 화이트리스트 경로 등록 후 필터에서 제외되도록 설정
        boolean isWhitelisted = EXCLUDE_URLS.stream().anyMatch(requestURI::matches);
        if (isWhitelisted) {
            filterChain.doFilter(request, response);
            return;
        }

        //3. Token 검증
        try {
            String accessToken = jwtHelper.resolveToken(request);
            if (accessToken != null) {
                jwtHelper.validateToken(accessToken);
                Authentication authentication = jwtHelper.getAuthenticationFromAccessToken(accessToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                    return;
                }
            } else {
                throw new CustomJwtException(ExceptionCode.NOT_FOUND_TOKEN);
            }
        } catch (CustomJwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());

            // response에 바로 에러 응답을 설정하여 필터 체인 중단
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(e.getHttpStatus().value());

            // ObjectMapper로 예쁘게 출력할 수 있도록 수정
            String errorResponse = String.format("{\"status\": \"%s\", \"message\": \"%s\", \"code\": \"%s\"}",
                    e.getHttpStatus(), e.getMessage(), e.getCode());

            response.getWriter().write(errorResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
