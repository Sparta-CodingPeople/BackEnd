package com.server.delivery.model.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_seoul_area_code")
public class SeoulAreaCode {

    @Id
    @Column(name = "seoul_area_code_id")
    private int seoulRegionCode;

    @Column(name = "area_name", nullable = false, length = 50)
    private String areaName;
}
