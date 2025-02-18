package com.server.delivery.model.master.repository;

import com.server.delivery.model.user.entity.User;

import java.util.Optional;

public interface MasterRepository {
    Optional<User> findById(String username);
}
