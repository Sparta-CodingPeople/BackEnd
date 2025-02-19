package com.server.delivery.model.master.repository;

import com.server.delivery.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MasterRepositoryImpl implements MasterRepository {
    private final MasterJparepository masterJparepository;

    @Override
    public Optional<User> findById(String username) {
        return masterJparepository.findByUsername(username);
    }
}
