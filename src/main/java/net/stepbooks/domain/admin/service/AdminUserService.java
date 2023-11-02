package net.stepbooks.domain.admin.service;

import net.stepbooks.domain.admin.entity.AdminUserEntity;
import net.stepbooks.application.dto.client.TokenDto;

public interface AdminUserService {

    AdminUserEntity findUserByEmail(String email);

    AdminUserEntity findUserByUsername(String username);

    void registerWithEmail(AdminUserEntity userEntity);

    TokenDto loginWithEmail(String email, String password);

    AdminUserEntity getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, AdminUserEntity userEntity);

}
