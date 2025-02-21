package com.server.delivery.model.ai.repository;

import com.server.delivery.model.ai.entity.AiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiResponseRepositoryImpl implements AiResponseRepository {
    private final AiResponseJpaRepository jpaRepository;

    @Override
    public AiResponse save(AiResponse aiResponse) {
        return jpaRepository.save(aiResponse);
    }
}
