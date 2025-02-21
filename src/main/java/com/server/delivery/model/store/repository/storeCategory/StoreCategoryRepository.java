package com.server.delivery.model.store.repository.storeCategory;

import com.server.delivery.model.store.entity.StoreCategory;

import java.util.List;

public interface StoreCategoryRepository {
    StoreCategory save(StoreCategory category);

    List<StoreCategory> saveAll(List<StoreCategory> storeCategoryList);
}
