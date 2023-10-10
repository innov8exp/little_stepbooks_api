package net.stepbooks.domain.user.service;

import net.stepbooks.interfaces.client.dto.SocialAuthDto;
import net.stepbooks.interfaces.client.dto.TokenDto;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.domain.user.entity.UserTagRefEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    Boolean existsUserByEmail(String email);

    UserEntity findUserByEmail(String email);

    UserEntity findUserByUsername(String username);

    void registerWithEmail(UserEntity userEntity);

    void resetPassword(UserEntity userEntity);

    TokenDto loginWithEmail(String email, String password);

    TokenDto guestLogin(String deviceId);

    TokenDto socialLogin(SocialAuthDto socialAuthDto);

    UserEntity findUserByFacebookId(String facebookUserId);

    UserEntity findUserByGoogleId(String googleUserId);

    UserEntity getUserInfoFromToken(String token);

    TokenDto refreshToken(String accessToken, String refreshToken);

    void updateUserById(String id, UserEntity userEntity);

    List<UserEntity> findUsers();

    UserEntity findUser(String id);

    void deleteUser(String id);

    void sendRegisterVerificationEmail(String email);

    void sendForgetPasswordVerificationEmail(String email);

    void sendLinkVerificationEmail(String email);

    void createUserTagRef(List<UserTagRefEntity> userTagRefEntities);

    String uploadImg(MultipartFile file, String userId);
}
