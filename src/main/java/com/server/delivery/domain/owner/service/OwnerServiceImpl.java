package com.server.delivery.domain.owner.service;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.owner.dto.request.UpdateManagerRequestDto;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private UserHelper userHelper;

    public OwnerServiceImpl(UserHelper userHelper) {
    }

    @Override
    public void updateManager(Long userId, UpdateManagerRequestDto updateManagerRequestDto, CustomUserDetail customUserDetail) {
        User userById = userHelper.getUserById(userId);
    }
}
