package com.server.delivery.model.store.repository.location;

import com.server.delivery.model.store.entity.Location;

public interface LocationRepository {
    Location save(Location location);
}
