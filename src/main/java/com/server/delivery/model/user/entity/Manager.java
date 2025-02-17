package com.server.delivery.model.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("where is_deleted = false")
@Table(name = "p_manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
}
