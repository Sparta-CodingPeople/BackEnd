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
@Table(name = "p_seoul_region_code")
public class SeoulRegionCode {

    @Id
    @Column(name = "seoul_region_code_id")
    private int seoulRegionCode;

    @Column(name = "seoul_code_category", nullable = false, length = 50)
    private String seoulCodeCategory;
}
