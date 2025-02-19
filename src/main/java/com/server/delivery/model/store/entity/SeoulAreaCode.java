package com.server.delivery.model.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_seoul_area_code")
public class SeoulAreaCode {

    @Id
    @Column(name = "seoul_area_code_id")
    private int seoulRegionCode;

    @Column(name = "area_name", nullable = false, length = 50)
    private String areaName;
}
