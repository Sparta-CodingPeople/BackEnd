package com.server.delivery.model.owner.repository;

import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OwnerRepositoryImpl implements OwnerRepository {
    private final OwnerJpaRepository ownerJpaRepository;

    @Override
    public Owner save(Owner owner) {
        return ownerJpaRepository.save(owner);
    }

    @Override
    public Optional<List<Owner>> findByUser(User user) {
        return ownerJpaRepository.findByUser(user);
    }
}
