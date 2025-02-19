package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
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
    private int id;

    @Column(name = "store_category", nullable = false)
    private String storeCategory;
}
