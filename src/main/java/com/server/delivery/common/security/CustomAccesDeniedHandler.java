package com.server.delivery.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.delivery.common.exception.ExceptionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

public class CustomAccesDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 요청 속성에서 메시지와 코드 가져오기
        String message = (String) request.getAttribute("message");
        int code = (request.getAttribute("code") != null) ? (int) request.getAttribute("code") : 4999;

        // 메시지가 없으면 기본 메시지 설정
        if (message == null) {
            message = "You do not have permission to access this resource";
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(ExceptionResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .status(HttpStatus.FORBIDDEN)
                .message(message)
                .code(code)
                .build());

        response.getWriter().write(result);
    }
}
