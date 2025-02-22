package com.server.delivery.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.delivery.common.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Get message and code from request attributes, defaulting to empty string and 0 if null
        String message = (String) request.getAttribute("message");
        int code = (request.getAttribute("code") != null) ? (int) request.getAttribute("code") : 4999;

        // If message is null, set a default message
        if (message == null) {
            message = "Invalid Authentication";
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request
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