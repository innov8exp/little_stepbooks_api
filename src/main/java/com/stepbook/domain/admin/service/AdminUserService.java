package com.stepbook.domain.admin.service;

import com.stepbook.domain.admin.entity.AdminUserEntity;
import com.stepbook.interfaces.client.dto.TokenDto;

public interface AdminUserService {

    AdminUserEntity findUserByEmail(String email);

    AdminUserEntity findUserByUsername(String username);

    void registerWithEmail(AdminUserEntity userEntity);

    TokenDto loginWithEmail(String email, String password);

    AdminUserEntity getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, AdminUserEntity userEntity);

}
