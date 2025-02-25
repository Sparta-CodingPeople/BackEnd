package com.server.delivery.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.delivery.domain.auth.dto.request.CustomerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.OwnerCreateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class AuthControllerSignUpTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 고객 회원가입 테스트
    @Test
    public void testCustomerSignUp() throws Exception {
        CustomerCreateRequestDto requestDto = CustomerCreateRequestDto.builder()
                .username("test123123")
                .password("Tester12@")
                .firstName("John")
                .lastName("Doe")
                .nickname("johnny")
                .phoneNumber("010-1234-5678")
                .gender(1)
                .birthDate("1990-01-01")
                .profileImage(null)
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MockMultipartFile file1 = new MockMultipartFile("customerCreateRequestDto", "customerCreateRequestDto", "application/json", requestJson.getBytes());

        mockMvc.perform(multipart("/api/v1/auth/sign-up/customer")
                        .file("profileImage", null)  // 빈 바이트 배열을 전송하여 실제 파일이 없음을 나타냄
                        .file(file1)  // 수정: param 대신 content로 요청 보내기
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("회원가입 성공"));
    }

    // 사업주 회원가입 테스트
    @Test
    @Transactional
    public void testOwnerSignUp() throws Exception {
        OwnerCreateRequestDto requestDto = OwnerCreateRequestDto.builder()
                .username("owner1")
                .password("Password123@")
                .firstName("John")
                .lastName("Doe")
                .nickname("johnny")
                .phoneNumber("010-1234-5678")
                .gender(1)
                .birthDate("1990-01-01")
                .businessNumber("1234")
                .build();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MockMultipartFile file1 = new MockMultipartFile("ownerCreateRequestDto", "customerCreateRequestDto", "application/json", requestJson.getBytes());

        mockMvc.perform(multipart("/api/v1/auth/sign-up/owner")
                        .file("profileImage", null)  // 빈 바이트 배열을 전송하여 실제 파일이 없음을 나타냄
                        .file(file1)  // 수정: param 대신 content로 요청 보내기
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("사업자 회원가입 성공"));
    }

}