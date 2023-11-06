package net.stepbooks.domain.user.service;

import net.stepbooks.application.dto.client.SocialAuthDto;
import net.stepbooks.application.dto.client.TokenDto;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    Boolean existsUserByEmail(String email);

    User findUserByEmail(String email);

    User findUserByPhone(String phone);

    User findUserByUsername(String username);

    void registerWithEmail(User user);

    void registerWithPhone(User user);

    void registerWithWechat(User user);

    void resetPassword(User user);

    TokenDto loginWithEmail(String email, String password);

    TokenDto loginWithSms(String phone, String verificationCode);

    TokenDto loginWithWechat(String code, String deviceId);

    TokenDto guestLogin(String deviceId);

    TokenDto socialLogin(SocialAuthDto socialAuthDto);

    User findUserByFacebookId(String facebookUserId);

    User findUserByGoogleId(String googleUserId);

    User getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, User user);

    List<User> findUsers();

    User findUser(String id);

    void deleteUser(String id);

    void sendRegisterVerificationEmail(String email);

    void sendForgetPasswordVerificationEmail(String email);

    void sendLinkVerificationEmail(String email);

    void sendLoginVerificationSms(String phone);

    Media uploadImg(MultipartFile file, String userId);
}