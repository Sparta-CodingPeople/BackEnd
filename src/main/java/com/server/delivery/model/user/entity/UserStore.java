package com.server.delivery.model.user.entity;

import com.server.delivery.model.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_user_store")
public class UserStore {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_store_uuid")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;


}
