package com.server.delivery.model.store.repository.userStore;

import com.server.delivery.model.user.entity.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserStoreRepositoryImpl implements UserStoreRepository {
    private final UserStoreJpaRepository userStoreJpaRepository;

    @Override
    public UserStore save(UserStore userStore) {
        return userStoreJpaRepository.save(userStore);
    }
}
