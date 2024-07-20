package net.stepbooks.domain.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.interfaces.client.dto.SocialAuthDto;
import net.stepbooks.interfaces.client.dto.TokenDto;
import net.stepbooks.interfaces.client.dto.UserDto;
import net.stepbooks.interfaces.client.dto.WechatAuthDto;

public interface UserService {

    Boolean existsUserByEmail(String email);

    User findUserByEmail(String email);

    User findUserByPhone(String phone);

    User findUserByWxUnionId(String unionId);

    User findUserByWxOpenId(String openId);

    User findUserByUsername(String username);

    void registerWithEmail(User user);

    void registerWithPhone(User user);

    void registerWithWechat(User user);

    void resetPassword(User user);

    TokenDto loginWithEmail(String email, String password);

    TokenDto loginWithSms(String phone, String verificationCode);

    TokenDto loginWithWechat(WechatAuthDto wechatAuthDto);

    TokenDto guestLogin(String deviceId);

    TokenDto socialLogin(SocialAuthDto socialAuthDto);

    User findUserByFacebookId(String facebookUserId);

    User findUserByGoogleId(String googleUserId);

    User getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, User user);

    IPage<User> findUsers(Page<User> page, String username, String nickname);

    User findUser(String id);

    void changeUserStatus(String id, Boolean active);

    void sendRegisterVerificationEmail(String email);

    void sendForgetPasswordVerificationEmail(String email);

    void sendLinkVerificationEmail(String email);

    void sendLoginVerificationSms(String phone);

    UserDto getUserAndChildAgeInfoByUsername(String username);

    User assignOpenId(WechatAuthDto wechatAuthDto, User user);
}
