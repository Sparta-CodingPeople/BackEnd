package com.server.delivery.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class CustomResponse<T> {
    private int status;
    private String message;
    private T body;

    public static <T> CustomResponse<T> success(String message, T data) {
        return (CustomResponse<T>) CustomResponse.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .body(data)
                .build();
    }

    public static <T> CustomResponse<T> success(String message) {
        return (CustomResponse<T>) CustomResponse.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .body(null)
                .build();
    }
}
