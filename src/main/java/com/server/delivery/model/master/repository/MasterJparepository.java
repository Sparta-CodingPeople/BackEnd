package com.server.delivery.model.master.repository;

import com.server.delivery.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterJparepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
