package com.server.delivery.common.security;

import com.server.delivery.common.filter.AuthenticationFilter;
import com.server.delivery.common.jwt.JwtHelper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtHelper jwtHelper) throws Exception {
        // http 공통 설정
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new AuthenticationFilter(jwtHelper), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        // 권한 설정
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/api/master/v1/sign-in")
                .permitAll() // 추가: Master 로그인 경로
                .requestMatchers("/api/v1/auth/sign-up", "/api/v1/auth/sign-in", "/api/v1/auth/renew")
                .permitAll() // 인증 없이 접근 가능한 경로들
                .requestMatchers("/api/owner/v1/auth/sign-up", "/api/v1/auth/sign-up/owner")
                .permitAll() // Owner 관련 경로 추가
                .requestMatchers("/api/v1/auth/sign-up/customer")
                .permitAll() // 고객 관련 경로 추가

                // 리뷰 관련 경로
                // 리뷰 생성 (CUSTOMER만 가능)
                .requestMatchers(HttpMethod.POST, "/api/v1/reviews")
                .hasAuthority("CUSTOMER")

                // 리뷰 수정 및 삭제 (MASTER만 가능) -> ref. 특정 기간 내에 CUSTOMER도 본인이 작성한 리뷰 수정 가능
                .requestMatchers(HttpMethod.PATCH, "/api/v1/reviews/{reviewId}")
                .hasAnyAuthority("MASTER", "CUSTOMER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/{reviewId}")
                .hasAuthority("MASTER")

                // Owner 관련 경로
                .requestMatchers(HttpMethod.PUT, "/api/v1/owner/{toBeManagerUserId}/toBeManagerUser")
                .hasAnyAuthority("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/owner/{ChangedUser}/ChangedUser")
                .hasAnyAuthority("OWNER", "MANAGER")

                // 매장 관련 경로
                .requestMatchers(HttpMethod.POST, "/api/v1/stores")
                .hasAnyAuthority("OWNER", "MASTER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/stores/**")
                .hasAnyAuthority("OWNER", "MASTER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/stores/**")
                .hasAnyAuthority("OWNER", "MASTER")

                // 메뉴 관련 경로
                .requestMatchers(HttpMethod.POST, "/api/v1/menus/{storeUuid}")
                .hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/menus/{menuUuid}/menu")
                .hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/menus/{menuUuid}/menu")
                .hasAnyAuthority("OWNER", "MANAGER", "MASTER")

                // 장바구니 관련 경로 (CUSTOMER, MASTER만 접근 가능)
                .requestMatchers(HttpMethod.POST, "/api/v1/cart")
                .hasAnyAuthority("CUSTOMER", "MASTER")  // 장바구니 추가
                .requestMatchers(HttpMethod.GET, "/api/v1/cart")
                .hasAnyAuthority("CUSTOMER", "MASTER")  // 장바구니 조회
                .requestMatchers(HttpMethod.DELETE, "/api/v1/cart")
                .hasAnyAuthority("CUSTOMER", "MASTER")  // 장바구니 삭제
                .requestMatchers(HttpMethod.PATCH, "/api/v1/cart/**")
                .hasAnyAuthority("CUSTOMER", "MASTER")  // 장바구니 수정

                // 주문 관련 경로
                .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/{orderUuid}/cancel")
                .hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/{orderUuid}/accept")
                .hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/{orderUuid}/reject")
                .hasAnyAuthority("OWNER", "MANAGER", "MASTER")

                // 결제 관련 경로
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/confirm")
                .hasAuthority("CUSTOMER")
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/{paymentId}/cancel")
                .hasAnyAuthority("CUSTOMER", "MASTER")
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/{paymentId}/confirm")
                .hasAuthority("MASTER")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments/{paymentId}")
                .hasAnyAuthority("CUSTOMER", "MASTER")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments")
                .hasAnyAuthority("CUSTOMER", "MASTER")

                // 기타 모든 요청은 인증 필요
                .anyRequest()
                .authenticated()
        );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList("X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
