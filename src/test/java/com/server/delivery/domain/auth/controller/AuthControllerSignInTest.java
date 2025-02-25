package com.server.delivery.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.delivery.domain.auth.dto.request.SignInRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@Transactional
public class AuthControllerSignInTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String accessToken;

    @BeforeEach
    public void setUp() throws Exception {

        SignInRequestDto requestDto = SignInRequestDto.builder()
                .username("tester1234")
                .password("Password123@")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // 로그인 후, 응답에서 토큰 추출
        MvcResult result = mockMvc.perform(post("/api/v1/auth/sign-in")
                        .content(requestJson)
                        .contentType("application/json"))
                .andExpect(status().isOk()) // 로그인 성공 응답 상태 코드 확인
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("로그인 성공")) // 응답 메시지 확인
                .andReturn();

        // 로그인 후 받은 토큰을 변수에 저장 (예시: access token)
        accessToken = result.getResponse().getHeader("Authorization");
        // 실제 토큰 구조에 맞게 필요한 값 추출 (예: JSON 파싱 후 토큰 추출)
        System.out.println("accessToken = " + accessToken);
    }

    // 로그인 테스트
    @Test
    public void testSignIn() throws Exception {
        SignInRequestDto requestDto = SignInRequestDto.builder()
                .username("tester1234")
                .password("Password123@")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/v1/auth/sign-in")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("로그인 성공"));
    }

    // 토큰 갱신 테스트
    @Test
    public void testRenewToken() throws Exception {
        // Assume you already have a valid token for the sake of this test

        System.out.println("accessToken =" + accessToken);

        mockMvc.perform(post("/api/v1/auth/renew")
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("토큰 갱신 성공"));
    }
}