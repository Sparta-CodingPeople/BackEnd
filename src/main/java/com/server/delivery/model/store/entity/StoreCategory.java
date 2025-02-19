package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store_category")
public class StoreCategory extends BaseEntity{

    @Id
    @Column(name = "store_category_id")
    private int storeCategoryId;

    @Column(name = "store_category", nullable = false)
    private String storeCategory;
}
