package com.server.delivery.model.owner.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_owner")
@SQLRestriction("owner_is_deleted = false")
public class Owner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "owner")  // 여러 개의 매장을 가질 수 있음
    private List<Store> stores;

    @Column(name = "business_number")
    private String businessNumber;

    @Builder.Default
    @Column(name = "owner_is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

}
