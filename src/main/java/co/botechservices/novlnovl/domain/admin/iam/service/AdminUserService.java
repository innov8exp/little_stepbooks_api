package co.botechservices.novlnovl.domain.admin.iam.service;

import co.botechservices.novlnovl.domain.admin.iam.entity.AdminUserEntity;
import co.botechservices.novlnovl.domain.user.dto.TokenDto;

public interface AdminUserService {

    AdminUserEntity findUserByEmail(String email);

    AdminUserEntity findUserByUsername(String username);

    void registerWithEmail(AdminUserEntity userEntity);

    TokenDto loginWithEmail(String email, String password);

    AdminUserEntity getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, AdminUserEntity userEntity);

}
