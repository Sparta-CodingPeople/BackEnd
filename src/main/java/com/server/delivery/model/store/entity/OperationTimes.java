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
@Table(name = "p_operation_times")
public class OperationTimes extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_operating_time_uuid")
    private UUID storeOperatingTimesUuid;

    @Column(name = "weekday", nullable = false)
    private int weekday;

    @Column(name = "operation_time_opening_time", nullable = false)
    private String operationTimeOpeningTime;

    @Column(name = "operation_time_closing_time", nullable = false)
    private String operationTimeClosingTime;

}
