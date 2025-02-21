package com.server.delivery.model.store.repository.userStore;

import com.server.delivery.model.user.entity.UserStore;

public interface UserStoreRepository {
    UserStore save(UserStore userStore);
}
