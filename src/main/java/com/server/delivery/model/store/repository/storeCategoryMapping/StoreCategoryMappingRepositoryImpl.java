package com.server.delivery.model.store.repository.storeCategoryMapping;

import com.server.delivery.model.store.entity.StoreCategoryMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StoreCategoryMappingRepositoryImpl implements StoreCategoryMappingRepository {
    private final StoreCategoryMapingJpaRepository storeCategoryMappingRepository;

    @Override
    public StoreCategoryMapping save(StoreCategoryMapping categoryMapping) {
        return storeCategoryMappingRepository.save(categoryMapping);
    }

    @Override
    public List<StoreCategoryMapping> saveAll(List<StoreCategoryMapping> categoryMappings) {
        return storeCategoryMappingRepository.saveAll(categoryMappings);
    }
}
