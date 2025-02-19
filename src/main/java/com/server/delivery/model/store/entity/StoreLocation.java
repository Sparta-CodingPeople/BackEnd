package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_store_location")
public class StoreLocation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_location_uuid")
    private UUID storeLocationUuid;

    @OneToOne(mappedBy = "storeLocation")
    private Store store;

    @OneToOne
    @JoinColumn(name = "location_uuid")
    private Location location;
}
