package com.server.delivery.model.master.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("where is_deleted = false")
@Table(name = "p_master")
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id", nullable = false, updatable = false)
    private String id;

    @Column(name = "master_master_code")
    private String masterCode;

}
