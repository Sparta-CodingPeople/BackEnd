package com.server.delivery.model.user.repository;

import com.server.delivery.model.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {
    User save(User any);

    Page<User> findByNicknameContaining(String keyword, Pageable sortedPageable);

    void delete(User user);

    Optional<User> findById(Long userId);
}
