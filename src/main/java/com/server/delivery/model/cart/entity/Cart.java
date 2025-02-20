package com.server.delivery.model.cart.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE p_cart SET cart_is_deleted = true WHERE cart_uuid = ?")
@SQLRestriction("where cart_is_deleted = false")
@Table(name = "p_cart")
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_uuid")
    private UUID cartUuid;

    @Column(name = "total_quantity")
    private int totalQuantity;

    @Column(name = "total_price")
    private int totalPrice;

    @OneToMany(mappedBy = "cart")
    private List<MenuCart> menuCarts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "cart_is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
