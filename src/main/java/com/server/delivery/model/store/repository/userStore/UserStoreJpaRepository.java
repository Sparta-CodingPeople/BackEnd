package com.server.delivery.model.store.repository.userStore;

import com.server.delivery.model.user.entity.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserStoreJpaRepository extends JpaRepository<UserStore, UUID> {
}
