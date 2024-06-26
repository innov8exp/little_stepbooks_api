package net.stepbooks.domain.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.interfaces.admin.dto.ResetPasswordDto;
import net.stepbooks.interfaces.client.dto.TokenDto;

public interface AdminUserService extends IService<AdminUser> {

    AdminUser findUserByEmail(String email);

    AdminUser findUserByPhone(String phone);

    AdminUser findUserByUsername(String username);

    void registerWithEmail(AdminUser userEntity);

    TokenDto loginWithEmail(String email, String password);

    AdminUser getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, AdminUser userEntity);

    void sendLoginVerificationSms(String phone);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    void changePassword(String id, String oldMd5, String newMd5);
}
