package com.server.delivery.model.ai.repository;

import com.server.delivery.model.ai.entity.Ai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiJpaRepository extends JpaRepository<Ai, Long> {

}
