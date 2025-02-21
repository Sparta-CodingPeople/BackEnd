package com.server.delivery.model.owner.entity;

import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_owner")
//TODO:: softDelete 구현하기
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "owner")
    private List<OwnerStore> ownerStore;

    @Column(name = "business_number")
    private String businessNumber;


}
