package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store_operation_times")
public class StoreOperationTimes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_operation_times_uuid")
    private UUID storeOperationTimesUuid;

    @ManyToOne
    @JoinColumn(name = "store_uuid", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "operation_times_uuid", nullable = false)
    private OperationTimes operationTimes;
}
