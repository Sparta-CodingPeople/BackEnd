package com.server.delivery.model.ai.entity;

import com.server.delivery.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_ai_response")
public class AiResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_response_id")
    private Long id;

    @Column(name ="ai_response_text", nullable = false, length = 150)
    private String responseText;


}
