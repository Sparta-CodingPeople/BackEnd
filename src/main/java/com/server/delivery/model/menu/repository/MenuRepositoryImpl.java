package com.server.delivery.model.menu.repository;

import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Menu save(Menu menu) {
        return menuJpaRepository.save(menu);
    }

    @Override
    public void delete(Menu menu) {
        menuJpaRepository.delete(menu);
    }

    @Override
    public Page<Menu> findByStoreAndMenuNameContainingAndMenuAvailabilityTrue(Store store, String keyword, Pageable pageable) {
        return menuJpaRepository.findByStoreAndMenuNameContainingAndMenuAvailabilityTrue(store, keyword, pageable);
    }

}
