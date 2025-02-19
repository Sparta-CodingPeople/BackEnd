package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_store_category")
public class StoreCategory extends BaseEntity {

    @Id
    @Column(name = "store_category_id")
    private int storeCategoryId;

    @Column(name = "store_category", nullable = false)
    private String storeCategory;
}
