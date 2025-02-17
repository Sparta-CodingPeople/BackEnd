package com.server.delivery.model.user.repository;

import com.server.delivery.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Page<User> findByNicknameContaining(String keyword, Pageable sortedPageable) {
        return jpaRepository.findByNicknameContaining(keyword, sortedPageable);
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(user);
    }
}
