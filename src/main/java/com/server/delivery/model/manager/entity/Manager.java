package com.server.delivery.model.manager.entity;

import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 매니저가 될 유저

    @ManyToOne
    @JoinColumn(name = "store_uuid")
    private Store store;  // 매니저가 속한 매장

}
