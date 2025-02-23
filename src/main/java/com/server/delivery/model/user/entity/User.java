package com.server.delivery.model.user.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.delivery.entity.DeliveryAddress;
import com.server.delivery.model.manager.entity.Manager;
import com.server.delivery.model.master.entity.Master;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.payment.Payment;
import com.server.delivery.model.review.entity.Review;
import com.server.delivery.model.user.entity.constant.UserGender;
import com.server.delivery.model.user.entity.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_users SET users_is_deleted = true WHERE users_id = ?")
@SQLRestriction("users_is_deleted = false")
@Table(name = "p_users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "users_username", nullable = false, unique = true)
    private String username;

    @Column(name = "users_password", nullable = false)
    private String password;

    @Column(name = "users_first_name", nullable = false)
    private String firstName;

    @Column(name = "users_last_name", nullable = false)
    private String lastName;

    @Column(name = "users_nickname", nullable = false)
    private String nickname;

    @Column(name = "users_phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "users_profile_image")
    private String profileImage;

    @Builder.Default
    @Column(name = "users_is_public", nullable = false)
    private Boolean isPublic = Boolean.FALSE;

    @Column(name = "users_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "users_gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Column(name = "users_birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "users_token_issued_at")
    private LocalDateTime tokenIssuedAt;

    @Column(name = "users_is_deleted")
    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    private Master master;

    @OneToMany(mappedBy = "user")
    private List<Owner> owners;

    @OneToMany(mappedBy = "user")
    private List<UserStore> userStores;

    @OneToMany(mappedBy = "user")
    private List<Manager> managers;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Payment> payments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

    public void updateTokenIssuedAt() {
        this.tokenIssuedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
