package com.server.delivery.model.store.repository.location;

import com.server.delivery.model.store.entity.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LocationRepositoryImpl implements LocationRepository {
    private final LocationJpaRepository locationJpaRepository;

    @Override
    public Location save(Location location) {
        return locationJpaRepository.save(location);
    }
}
