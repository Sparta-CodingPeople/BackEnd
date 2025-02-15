package com.server.delivery.model.user.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.user.entity.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete
@SQLRestriction("where is_deleted = false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_password")
    private String password;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    @Column(name = "user_role")
    private Role role;

    @PreRemove
    public void preRemove() {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(this.name);
    }

}
