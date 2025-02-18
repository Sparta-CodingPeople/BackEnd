package com.server.delivery.domain.auth.service;

import com.server.delivery.common.jwt.CustomUserDetail;
import com.server.delivery.domain.auth.dto.request.CustomerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.OwnerCreateRequestDto;
import com.server.delivery.domain.auth.dto.request.SignInRequestDto;
import com.server.delivery.domain.user.dto.request.MasterSignInRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    void createCustomerAccount(CustomerCreateRequestDto customerCreateRequestDto, MultipartFile profileImage);

    void createOwnerAccount(OwnerCreateRequestDto ownerCreateRequestDto, MultipartFile profileImage);

    String signInCustomer(SignInRequestDto signInRequestDto);

    String signInMaster(MasterSignInRequestDto signInRequestDto);

    String renewToken(CustomUserDetail customUserDetail);
}
