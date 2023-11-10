package net.stepbooks.domain.user.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.domain.email.service.impl.EmailServiceImpl;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.media.service.FileService;
import net.stepbooks.domain.sms.service.SmsService;
import net.stepbooks.domain.user.entity.AuthHistory;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.mapper.AuthHistoryMapper;
import net.stepbooks.domain.user.mapper.UserMapper;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.enums.RoleEnum;
import net.stepbooks.infrastructure.enums.SmsType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.external.client.FacebookClient;
import net.stepbooks.infrastructure.external.client.GoogleClient;
import net.stepbooks.infrastructure.external.client.WechatClient;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.security.user.UserJwtTokenProvider;
import net.stepbooks.infrastructure.util.CommonUtil;
import net.stepbooks.interfaces.client.assembler.AuthAssembler;
import net.stepbooks.interfaces.client.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String STORE_PATH = "user/avatar/images/";
    private final UserMapper userMapper;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final AuthHistoryMapper authHistoryMapper;
    private final FileService privateFileServiceImpl;
    private final FacebookClient facebookClient;
    private final GoogleClient googleClient;
    private final SmsService smsService;
    private final WechatClient wechatClient;


    @Value("${facebook.client-id}")
    private String facebookClientId;
    @Value("${facebook.client-secret}")
    private String facebookClientSecret;

    @SuppressWarnings("checkstyle:MagicNumber")
    private static String generateVerifyCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }

    @Override
    public Boolean existsUserByEmail(String email) {
        Long count = userMapper.selectCount(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        return count > 0;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with email: " + email);
        }
        return user;
    }

    @Override
    public User findUserByPhone(String phone) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with username: " + username);
        }
        return user;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Transactional
    @Override
    public void registerWithEmail(User user) {
        String email = user.getEmail();
        User isUserExist = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
        if (!ObjectUtils.isEmpty(isUserExist)) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS_ERROR,
                    "Email: " + email + " has been registered, please select another one.");
        }
        String password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword());
        user.setPassword(password);
        user.setRole(RoleEnum.NORMAL_USER.getValue());
        user.setNickname("StepBooks" + CommonUtil.getStringRandom(8));
        user.setUsername(UUID.randomUUID().toString());
        int insert = userMapper.insert(user);
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setAuthType(AuthType.EMAIL);
        authHistory.setEmail(email);
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public void registerWithPhone(User user) {
        String phone = user.getPhone();
        user.setRole(RoleEnum.NORMAL_USER.getValue());
        user.setNickname("StepBooks" + CommonUtil.getStringRandom(8));
        user.setUsername(UUID.randomUUID().toString());
        int insert = userMapper.insert(user);
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setAuthType(AuthType.PHONE);
        authHistory.setPhone(phone);
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public void registerWithWechat(User user) {
        String wechatId = user.getWechatId();
        user.setRole(RoleEnum.NORMAL_USER.getValue());
        user.setNickname("StepBooks" + CommonUtil.getStringRandom(8));
        user.setUsername(UUID.randomUUID().toString());
        int insert = userMapper.insert(user);
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setAuthType(AuthType.WECHAT);
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
    }

    @Transactional
    @Override
    public void resetPassword(User user) {
        String email = user.getEmail();
        String password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(user.getPassword());
        user.setPassword(password);
        int update = userMapper.updateById(user);
        AuthHistory authHistory = new AuthHistory();
        authHistory.setAuthType(AuthType.EMAIL);
        authHistory.setUsername(user.getUsername());
        authHistory.setEmail(email);
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        if (update != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Reset password error");
        }
    }

    @Override
    public TokenDto loginWithEmail(String email, String password) {
        User user = this.findUserByEmail(email);
        var valid = PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(password, user.getPassword());
        if (!valid) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "auth failed, please input the correct password");
        }
        Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistory>lambdaQuery()
                .eq(AuthHistory::getEmail, email));
        return getTokenDto(authCount, user, AuthType.EMAIL);
    }

    @Override
    public TokenDto loginWithSms(String phone, String verificationCode) {
        User user = findUserByPhone(phone);
        if (ObjectUtils.isEmpty(user)) {
            registerWithPhone(user);
        }
        Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistory>lambdaQuery()
                .eq(AuthHistory::getEmail, phone));
        return getTokenDto(authCount, user, AuthType.PHONE);
    }

    @Override
    public TokenDto loginWithWechat(String code, String deviceId) {
        return null;
    }

    @Override
    public TokenDto guestLogin(String deviceId) {
        List<User> userEntities = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getDeviceId, deviceId)
                .eq(User::getRole, RoleEnum.GUEST.getValue()));
        User user;
        if (!ObjectUtils.isEmpty(userEntities)) {
            user = userEntities.get(0);
        } else {
            user = User.builder()
                    .role(RoleEnum.GUEST.getValue())
                    .deviceId(deviceId)
                    .username(UUID.randomUUID().toString())
                    .nickname("Guest User").build();
            int insert = userMapper.insert(user);
            if (insert != 1) {
                throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
            }
        }
        return getTokenDto(0L, user, AuthType.GUEST);
    }

    private TokenDto getTokenDto(Long authCount, User user, AuthType authType) {
        JwtUserDetails jwtUserDetails = AuthAssembler.userEntityToJwtUserDetails(user);
        TokenDto tokenDto = jwtTokenProvider.generateToken(jwtUserDetails, authType);
        // First time to login
        tokenDto.setIsFirstAuth(authCount == 0);
        tokenDto.setRole(user.getRole());
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setAuthType(authType);
        authHistory.setEmail(user.getEmail());
        authHistory.setFacebookId(user.getFacebookId());
        authHistory.setGoogleId(user.getGoogleId());
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        return tokenDto;
    }

    @Transactional
    @Override
    public TokenDto socialLogin(SocialAuthDto socialAuthDto) {
        if (socialAuthDto.getAuthType().equals(AuthType.FACEBOOK.getValue())) {
            String inputToken = socialAuthDto.getToken();
            SocialTokenDto socialTokenDto = authToFacebook();
            String accessToken = socialTokenDto.getAccessToken();
            FacebookAuthResDto facebookAuthResDto = introspectFacebook(inputToken, accessToken);
            if (facebookAuthResDto.getData().isValid()) {
                String facebookId = facebookAuthResDto.getData().getUserId();
                User user = this.findUserByFacebookId(facebookId);
                if (user != null) {
                    Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistory>lambdaQuery()
                            .eq(AuthHistory::getFacebookId, facebookId));
                    return getTokenDto(authCount, user, AuthType.FACEBOOK);
                } else {
                    FacebookUserDto facebookUserInfo = getFacebookUserInfo(facebookId, accessToken);
                    user = registerUserFromFacebook(socialAuthDto.getDeviceId(), facebookId, facebookUserInfo);
                    JwtUserDetails jwtUserDetails = AuthAssembler.userEntityToJwtUserDetails(user);
                    TokenDto token = jwtTokenProvider.generateToken(jwtUserDetails, AuthType.FACEBOOK);
                    token.setIsFirstAuth(true);
                    return token;
                }
            }
        } else if (socialAuthDto.getAuthType().equals(AuthType.GOOGLE.getValue())) {
            String idToken = socialAuthDto.getToken();
            GoogleUserDto googleUserDto = introspectGoogle(idToken);
            if (googleUserDto != null) {
                String googleId = googleUserDto.getSub();
                User user = this.findUserByGoogleId(googleId);
                if (user != null) {
                    Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistory>lambdaQuery()
                            .eq(AuthHistory::getGoogleId, googleId));
                    return getTokenDto(authCount, user, AuthType.GOOGLE);
                } else {
                    user = registerUserFromGoogle(socialAuthDto.getDeviceId(), googleId, googleUserDto);
                    JwtUserDetails jwtUserDetails = AuthAssembler.userEntityToJwtUserDetails(user);
                    TokenDto token = jwtTokenProvider.generateToken(jwtUserDetails, AuthType.GOOGLE);
                    token.setIsFirstAuth(true);
                    return token;
                }
            }
        }
        throw new BusinessException(ErrorCode.AUTH_ERROR, "Failed auth with facebook");
    }

    private User registerUserFromFacebook(String deviceId, String facebookId, FacebookUserDto facebookUserInfo) {
        User user = User.builder()
                .role(RoleEnum.NORMAL_USER.getValue())
                .username(UUID.randomUUID().toString())
                .nickname(facebookUserInfo.getName())
                .facebookId(facebookId)
                .avatarImgUrl(facebookUserInfo.getPicture().getData().getUrl())
                .deviceId(deviceId)
                .build();
        int insert = userMapper.insert(user);
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setAuthType(AuthType.FACEBOOK);
        authHistory.setFacebookId(facebookId);
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
        return user;
    }

    private User registerUserFromGoogle(String deviceId, String googleId, GoogleUserDto googleUserDto) {
        User user = User.builder()
                .role(RoleEnum.NORMAL_USER.getValue())
                .username(UUID.randomUUID().toString())
                .nickname(googleUserDto.getName())
//                .email(googleUserDto.getEmail())
                .googleId(googleId)
                .avatarImgUrl(googleUserDto.getPicture())
                .deviceId(deviceId)
                .build();
        int insert = userMapper.insert(user);
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setAuthType(AuthType.GOOGLE);
        authHistory.setGoogleId(googleId);
        authHistory.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistory);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
        return user;
    }

    private GoogleUserDto introspectGoogle(String idToken) {
        return googleClient.introspectGoogle(idToken);
    }

    private SocialTokenDto authToFacebook() {
        return facebookClient.authToFacebook(facebookClientId, facebookClientSecret, "client_credentials");
    }

    private FacebookAuthResDto introspectFacebook(String inputToken, String accessToken) {
        return facebookClient.introspectFacebook(inputToken, accessToken);
    }

    private FacebookUserDto getFacebookUserInfo(String facebookId, String accessToken) {
        return facebookClient.getFacebookUserInfo(facebookId, "id,name,email,picture", accessToken);
    }

    @Override
    public User findUserByFacebookId(String facebookUserId) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getFacebookId, facebookUserId));
    }

    @Override
    public User findUserByGoogleId(String googleUserId) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getGoogleId, googleUserId));
    }

    @Override
    public User getUserInfoFromToken(String token) {
        return null;
    }

    @Override
    public TokenDto refreshToken(String accessToken, String refreshToken) {
        String username = jwtTokenProvider.getSubjectFromToken(accessToken);
        Map<String, Claim> claims = jwtTokenProvider.getClaimsFromToken(accessToken);
        User user = this.findUserByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        JwtUserDetails userDetails = JwtUserDetails.builder()
                .username(username)
                .email(user.getUsername())
                .nickname(user.getNickname())
                .googleId(user.getGoogleId())
                .facebookId(user.getFacebookId())
                .build();
        return jwtTokenProvider.refreshToken(userDetails, refreshToken, AuthType.valueOf(claims.get("authType").asString()),
                user.getModifiedAt());
    }

    @Override
    public void updateUserById(String id, User updatedUser) {
        User user = userMapper.selectById(id);
        BeanUtils.copyProperties(updatedUser, user, CommonUtil.getNullPropertyNames(updatedUser));
        userMapper.updateById(user);
    }

    @Override
    public List<User> findUsers() {
        return userMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public User findUser(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public void deleteUser(String id) {
        User user = userMapper.selectById(id);
        user.setActive(false);
        userMapper.updateById(user);
    }

    @Override
    public void sendRegisterVerificationEmail(String email) {
        String verifyCode = generateVerifyCode();
        String htmlBody = "<h1>NovlNovl Register Verification Code</h1>"
                + "<p>Your verification code is:<p>"
                + "<h2>" + verifyCode + "</h2>"
                + "<p>Please filled in the code into your App, it's only valid in "
                + "<red>" + EmailServiceImpl.VERIFICATION_CODE_VALID_SECONDS + "</red> seconds.</p>";
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("NovlNovl - Email Address Register Verification");
        emailDto.setCode(verifyCode);
        emailDto.setText(htmlBody);
        emailDto.setEmailType(EmailType.REGISTER);
        log.info(emailDto.getCode());
        emailService.sendSimpleMessage(emailDto);
    }

    @Override
    public void sendForgetPasswordVerificationEmail(String email) {
        String verifyCode = generateVerifyCode();
        String htmlBody = "<h1>NovlNovl Reset Password Verification Code</h1>"
                + "<p>Your verification code is:<p>"
                + "<h2>" + verifyCode + "</h2>"
                + "<p>Please filled in the code into your App, it's only valid in "
                + "<red>" + EmailServiceImpl.VERIFICATION_CODE_VALID_SECONDS + "</red> seconds.</p>";
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("NovlNovl - Reset Password Verification");
        emailDto.setCode(verifyCode);
        emailDto.setText(htmlBody);
        emailDto.setEmailType(EmailType.FORGET);
        log.info(emailDto.getCode());
        emailService.sendSimpleMessage(emailDto);
    }

    @Override
    public void sendLinkVerificationEmail(String email) {
        String verifyCode = generateVerifyCode();
        String htmlBody = "<h1>NovlNovl Link Email Verification Code</h1>"
                + "<p>Your verification code is:<p>"
                + "<h2>" + verifyCode + "</h2>"
                + "<p>Please filled in the code into your App, it's only valid in "
                + "<red>" + EmailServiceImpl.VERIFICATION_CODE_VALID_SECONDS + "</red> seconds.</p>";
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("NovlNovl - Link Email Address Verification");
        emailDto.setCode(verifyCode);
        emailDto.setText(htmlBody);
        emailDto.setEmailType(EmailType.LINK);
        log.info(emailDto.getCode());
        emailService.sendSimpleMessage(emailDto);
    }

    @Override
    public void sendLoginVerificationSms(String phone) {
        String verifyCode = generateVerifyCode();
        smsService.sendSms(SmsType.VERIFICATION, phone, verifyCode);
    }

    @Override
    public Media uploadImg(MultipartFile file, String userId) {
        String filename = file.getOriginalFilename();
        Media media = privateFileServiceImpl.upload(file, filename, STORE_PATH);
        User user = User.builder()
                .avatarImgId(media.getId())
                .avatarImgUrl(media.getObjectUrl()).build();
        updateUserById(userId, user);
        return media;
    }

}
