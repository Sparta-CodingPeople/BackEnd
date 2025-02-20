package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.util.List;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_operation_times")
public class OperationTimes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "store_operating_times_uuid")
    private UUID storeOperatingTimesUuid;

    @ElementCollection
    @Column(name = "weekday", nullable = false)
    private List<Integer> weekday;

    @Column(name = "operation_times_opening_time", nullable = false)
    private String operationTimeOpeningTime;

    @Column(name = "operation_times_closing_time", nullable = false)
    private String operationTimeClosingTime;

    @Column(name = "operation_times_is_holiday", nullable = false)
    private boolean isHoliday;

}
