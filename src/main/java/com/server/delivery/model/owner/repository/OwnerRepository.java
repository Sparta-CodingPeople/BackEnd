package com.server.delivery.model.owner.repository;

import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository {
    Owner save(Owner owner);

    Optional<List<Owner>> findByUser(User user);
}
