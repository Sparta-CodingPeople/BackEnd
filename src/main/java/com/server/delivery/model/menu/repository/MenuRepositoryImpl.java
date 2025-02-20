package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepository {
    private final MenuJpaRepository menuJpaRepository;

    @Override
    public Optional<Menu> findByMenuUuId(UUID productId) {
        return menuJpaRepository.findByMenuUuId(productId);
    }

}
