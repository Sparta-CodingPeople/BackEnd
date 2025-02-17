package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_store_operating_hours")
public class StoreOperatingHours extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_operating_hours_id")
    private int storeOperatingHoursId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "weekdays", nullable = false)
    private String weekdays;

    @Column(name = "opening_time", nullable = false)
    private String openingTime;

    @Column(name = "closing_time", nullable = false)
    private String closingTime;

    @Column(name = "is_closed", nullable = false)
    private boolean isClosed = Boolean.FALSE;
}
