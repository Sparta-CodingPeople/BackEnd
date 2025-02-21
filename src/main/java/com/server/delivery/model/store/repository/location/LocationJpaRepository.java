package com.server.delivery.model.store.repository.location;

import com.server.delivery.model.store.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationJpaRepository extends JpaRepository<Location, UUID> {
}
