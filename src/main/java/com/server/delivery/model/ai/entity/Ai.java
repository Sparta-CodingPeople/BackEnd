package com.server.delivery.model.ai.entity;

import com.server.delivery.common.BaseEntity;
import com.server.delivery.model.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_ai")
public class Ai extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_id")
    private Long id;

    @Column(name = "ai_response_text", nullable = false, length = 150)
    private String responseText;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

}
