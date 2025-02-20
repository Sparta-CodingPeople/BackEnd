package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;

import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {
    Optional<Menu> findByMenuUuId(UUID productUuid);
}
