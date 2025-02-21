package com.server.delivery.model.ai.repository;

import com.server.delivery.model.ai.entity.AiResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiResponseJpaRepository extends JpaRepository<AiResponse, Long> {

}
