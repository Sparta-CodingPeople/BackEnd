package com.server.delivery.model.master.entity;

import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_master")
public class Master {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "master_master_code")
    private String masterCode;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
