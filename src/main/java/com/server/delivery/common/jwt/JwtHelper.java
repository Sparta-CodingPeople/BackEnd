package com.server.delivery.common.jwt;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomJwtException;
import com.server.delivery.common.exception.customException.CustomUserException;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.repository.UserJpaRepository;
import com.server.delivery.util.helper.UserHelper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtHelper {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    private final UserJpaRepository userRepository;
    private final UserHelper userHelper;

    // 어세스 토큰 만료 시간
    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            log.info("validateToken 검증 시작");
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            //4. 사용자 정보 확인
            String username = getAuthenticationFromAccessToken(token).getName();
            User user = userRepository.findByUsername(username).orElseThrow(
                    () -> new CustomUserException(ExceptionCode.USER_NOT_FOUND)
            );

            // JWT 발급 시간과 token_issued_at 비교
            LocalDateTime tokenIssuedAt = LocalDateTime.parse(claims.getBody().get("tokenIssuedAt").toString());
            log.info(tokenIssuedAt.truncatedTo(ChronoUnit.SECONDS).toString());
            log.info(user.getTokenIssuedAt().truncatedTo(ChronoUnit.SECONDS).toString());
            if (tokenIssuedAt != null && tokenIssuedAt.truncatedTo(ChronoUnit.SECONDS).isEqual(user.getTokenIssuedAt().truncatedTo(ChronoUnit.SECONDS))) {
                return true;
            } else {
                log.error("JWT token issued time is earlier than token_issued_at in user data.");
                throw new CustomJwtException(ExceptionCode.TOKEN_IS_INVALID);
            }
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomJwtException(ExceptionCode.TOKEN_IS_INVALID);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new CustomJwtException(ExceptionCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomJwtException(ExceptionCode.TOKEN_IS_NOT_SUPPORTED);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomJwtException(ExceptionCode.TOKEN_IS_EMPTY);
        }
    }

    // JWT에서 인증 정보 추출
    public Authentication getAuthenticationFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("role") == null || claims.get("id") == null || claims.getSubject() == null) {
            throw new CustomJwtException(ExceptionCode.TOKEN_IS_INVALID);
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        CustomUserDetail userDetail = new CustomUserDetail(
                Long.parseLong(String.valueOf(claims.get("id"))),
                claims.getSubject(),
                "",
                authorities
        );

        return new UsernamePasswordAuthenticationToken(userDetail, "", authorities);
    }

    public User getUserFromToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String username = claims.getSubject();
        if (claims.get("id") == null) {
            throw new CustomJwtException(ExceptionCode.TOKEN_IS_INVALID);
        }

        return userHelper.getUser(username);

    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}

