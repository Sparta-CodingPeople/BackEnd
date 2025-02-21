package com.server.delivery.model.ai.repository;

import com.server.delivery.model.ai.entity.Ai;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiRepositoryImpl implements AiRepository {

    private final AiJpaRepository jpaRepository;

    @Override
    public Ai save(Ai ai) {
        return jpaRepository.save(ai);
    }

}
