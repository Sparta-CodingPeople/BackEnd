package com.server.delivery.model.master.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MasterRepositoryImpl implements MasterRepository {
    private final MasterJparepository masterJparepository;

}
