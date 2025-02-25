package com.server.delivery.model.store.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.store.constant.SeoulAreaCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("location_is_deleted = false")
@Table(name = "p_location")
public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "location_uuid")
    private UUID locationUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_seoul_area_code")
    private SeoulAreaCode seoulAreaCode;

    @Column(name = "location_seoul_address", nullable = false)
    private String address;

    @Column(name = "location_seoul_zipcode", nullable = false, length = 10)
    private int zipcode;

    @OneToMany(mappedBy = "location")
    private List<Store> stores;

    @Column(name = "location_is_deleted")
    @Builder.Default
    private boolean isDeleted = Boolean.FALSE;

}
