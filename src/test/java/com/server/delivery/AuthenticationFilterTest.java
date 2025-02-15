package com.server.delivery;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomJwtException;
import com.server.delivery.common.filter.AuthenticationFilter;
import com.server.delivery.common.jwt.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationFilterTest {

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 인증_성공_토큰이_정상적인_경우() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        when(jwtHelper.resolveToken(request)).thenReturn("valid-token");
        when(jwtHelper.validateToken("valid-token")).thenReturn(true);
        when(jwtHelper.getAuthenticationFromAccessToken("valid-token")).thenReturn(authentication);

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void 인증_실패_토큰이_유효하지_않은_경우() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        when(jwtHelper.resolveToken(request)).thenReturn("invalid-token");
        when(jwtHelper.validateToken("invalid-token")).thenThrow(new CustomJwtException(ExceptionCode.TOKEN_IS_INVALID));

        // When & Then
        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> {
            authenticationFilter.doFilterInternal(request, response, filterChain);
        });

        assertEquals("Token is invalid.", exception.getMessage());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void 화이트리스트_경로_필터_예외처리() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/v1/auth/sign-up");

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtHelper, never()).resolveToken(any());
    }


}