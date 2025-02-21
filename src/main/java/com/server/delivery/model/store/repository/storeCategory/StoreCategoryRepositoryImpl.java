package com.server.delivery.model.store.repository.storeCategory;

import com.server.delivery.model.store.entity.StoreCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreCategoryRepositoryImpl implements StoreCategoryRepository {
    private final StoreCategoryJpaRepository storeCategoryJpaRepository;

    @Override
    public StoreCategory save(StoreCategory category) {
        return storeCategoryJpaRepository.save(category);
    }

    @Override
    public List<StoreCategory> saveAll(List<StoreCategory> storeCategoryList) {
        return storeCategoryJpaRepository.saveAll(storeCategoryList);
    }
}
