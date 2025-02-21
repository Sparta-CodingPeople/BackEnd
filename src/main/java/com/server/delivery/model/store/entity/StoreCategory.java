package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.store.constant.StoreType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("store_category_is_deleted = false")
@Table(name = "p_store_category")
public class StoreCategory extends BaseEntity {

    @Id
    @Column(name = "store_category_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID storeCategoryId;

    @Column(name = "store_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Builder.Default
    @Column(name = "store_category_is_deleted")
    private boolean isDeleted = Boolean.FALSE;
}
