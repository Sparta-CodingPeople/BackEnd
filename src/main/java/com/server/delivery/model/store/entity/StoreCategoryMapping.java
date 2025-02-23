package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
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
@Table(name = "store_category_mapping")
@SQLRestriction("store_category_mapping_is_deleted = false")
public class StoreCategoryMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_category_mapping_uuid")
    private UUID storeCategoryMappingUuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_uuid", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_category_id", nullable = false)
    private StoreCategory storeCategory;

    @Builder.Default
    @Column(name = "store_category_mapping_is_deleted")
    private boolean isDeleted = Boolean.FALSE;

}