package com.server.delivery.model.owner.repository;

import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerJpaRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUser(User user);
}
