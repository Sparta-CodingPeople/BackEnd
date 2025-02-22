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
                .requestMatchers("/api/master/v1/sign-in").permitAll() // 추가: Master 로그인 경로
                .requestMatchers("/api/v1/auth/sign-up", "/api/v1/auth/sign-in", "/api/v1/auth/renew").permitAll() // 인증 없이 접근 가능한 경로들
                .requestMatchers("/api/owner/v1/auth/sign-up", "/api/v1/auth/sign-up/owner").permitAll() // Owner 관련 경로 추가
                .requestMatchers("/api/v1/auth/sign-up/customer").permitAll() // 고객 관련 경로 추가

                // 공개된 경로 설정
                .requestMatchers("/api/v1/reviews/**", "/api/v1/reviews/stores/**").hasAnyAuthority("CUSTOMER", "OWNER", "MANAGER", "MASTER")
                .requestMatchers("/api/v1/users/{userId}/reviews").hasAnyAuthority("CUSTOMER", "OWNER", "MASTER")

                // Master 관련 경로
                .requestMatchers("/api/master/v1/stores/**").hasAuthority("MASTER")

                // Owner 관련 경로
                .requestMatchers(HttpMethod.PUT, "/api/v1/owner/{toBeManagerUserId}/toBeManagerUser").hasAnyAuthority("OWNER", "MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/owner/{ChangedUser}/ChangedUser").hasAnyAuthority("OWNER", "MANAGER")

                // 매장 관련 경로
                .requestMatchers(HttpMethod.POST, "/api/v1/stores").hasAnyAuthority("OWNER", "MASTER")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/stores/**").hasAnyAuthority("OWNER", "MASTER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/stores/**").hasAnyAuthority("OWNER", "MASTER")

                // 메뉴 관련 경로
                .requestMatchers(HttpMethod.POST, "/api/v1/menus/{storeUuid}").hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/menus/{menuUuid}/menu").hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/menus/{menuUuid}/menu").hasAnyAuthority("OWNER", "MANAGER", "MASTER")

                // 주문 관련 경로
                .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/{orderUuid}/cancel").hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/{orderUuid}/accept").hasAnyAuthority("OWNER", "MANAGER", "MASTER")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/{orderUuid}/reject").hasAnyAuthority("OWNER", "MANAGER", "MASTER")

                // 결제 관련 경로
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/confirm").hasAuthority("CUSTOMER")
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/{paymentId}/cancel").hasAnyAuthority("CUSTOMER", "MASTER")
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/{paymentId}/confirm").hasAuthority("MASTER")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments/{paymentId}").hasAnyAuthority("CUSTOMER", "MASTER")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments").hasAnyAuthority("CUSTOMER", "MASTER")

                // 기타 모든 요청은 인증 필요
                .anyRequest().authenticated()
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
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token"));
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