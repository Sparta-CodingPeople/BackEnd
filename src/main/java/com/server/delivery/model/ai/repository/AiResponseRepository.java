package com.server.delivery.model.ai.repository;

import com.server.delivery.model.ai.entity.AiResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface AiResponseRepository {

    AiResponse save(AiResponse aiResponse);
}
