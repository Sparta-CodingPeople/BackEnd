package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_location")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "location_uuid")
    private UUID locationUuid;

    @OneToOne
    @JoinColumn(name = "location_category_id", nullable = false)
    private SeoulAreaCode locationCategory;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "zipcode", nullable = false, length = 10)
    private String zipcode;

}
