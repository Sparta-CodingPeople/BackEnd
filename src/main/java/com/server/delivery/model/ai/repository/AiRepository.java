package com.server.delivery.model.ai.repository;

import com.server.delivery.model.ai.entity.Ai;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository {

    Ai save(Ai ai);

}
