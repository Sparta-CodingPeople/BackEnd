package com.server.delivery.util.helper;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomUserException;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {
    private final UserRepository userRepository;

    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new CustomUserException(ExceptionCode.USER_NOT_FOUND)
                );
    }

    public void validateUser(String username) {
        userRepository.findByUsername(username)
                .ifPresentOrElse(
                        user -> {
                            throw new CustomUserException(ExceptionCode.USERNAME_IS_EXIST);
                        },
                        () -> {
                        } // 사용자가 존재하지 않을 경우 아무 동작도 수행하지 않음
                );
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomUserException(ExceptionCode.USER_NOT_FOUND)
        );
    }
}
