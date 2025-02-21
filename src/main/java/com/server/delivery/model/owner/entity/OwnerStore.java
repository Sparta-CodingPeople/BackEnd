package com.server.delivery.model.owner.entity;

import com.server.delivery.model.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_owner_store")
public class OwnerStore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ownerStoreUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    private Owner owner;
}
