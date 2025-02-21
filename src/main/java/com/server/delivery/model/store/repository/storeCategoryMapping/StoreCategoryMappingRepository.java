package com.server.delivery.model.store.repository.storeCategoryMapping;

import com.server.delivery.model.store.entity.StoreCategoryMapping;

import java.util.List;

public interface StoreCategoryMappingRepository {
    StoreCategoryMapping save(StoreCategoryMapping categoryMapping);

    List<StoreCategoryMapping> saveAll(List<StoreCategoryMapping> categoryMappings);
}
