package com.server.delivery.model.master.entity;

import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("master_is_delted = false")
@Table(name = "p_master")
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "master_master_code")
    private String masterCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Column(name = "master_is_deleted")
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;

}
