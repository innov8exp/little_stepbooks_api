package net.stepbooks.domain.user.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.email.service.EmailBusinessService;
import net.stepbooks.domain.email.service.impl.EmailBusinessServiceImpl;
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
import net.stepbooks.infrastructure.external.dto.UserPhoneNumberDto;
import net.stepbooks.infrastructure.external.dto.WechatGetAccessTokenResponse;
import net.stepbooks.infrastructure.external.dto.WechatLoginResponse;
import net.stepbooks.infrastructure.external.dto.WechatPhoneResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserJwtTokenProvider jwtTokenProvider;
    private final EmailBusinessService emailBusinessService;
    private final AuthHistoryMapper authHistoryMapper;
    private final FacebookClient facebookClient;
    private final GoogleClient googleClient;
    private final SmsService smsService;
    private final WechatClient wechatClient;


    @Value("${facebook.client-id}")
    private String facebookClientId;
    @Value("${facebook.client-secret}")
    private String facebookClientSecret;

    @Value("${wechat.appId}")
    private String wechatAppId;
    @Value("${wechat.secret}")
    private String wechatAppSecret;

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
    public User findUserByWxUnionId(String unionId) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUnionId, unionId));
    }

    @Override
    public User findUserByWxOpenId(String openId) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenId, openId));
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
        user.setRole(RoleEnum.NORMAL_USER);
        if (user.getNickname() == null) {
            user.setNickname("StepBooks" + CommonUtil.getStringRandom(8));
        }
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
        user.setRole(RoleEnum.NORMAL_USER);
        if (user.getNickname() == null) {
            user.setNickname("StepBooks" + CommonUtil.getStringRandom(8));
        }
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
        user.setRole(RoleEnum.NORMAL_USER);
        if (user.getNickname() == null) {
            user.setNickname("StepBooks" + CommonUtil.getStringRandom(8));
        }
        int insert = userMapper.insert(user);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
        AuthHistory authHistory = new AuthHistory();
        authHistory.setUsername(user.getUsername());
        authHistory.setPhone(user.getPhone());
        authHistory.setAuthType(AuthType.WECHAT);
        authHistoryMapper.insert(authHistory);
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

    private String getPhoneNumber(String accessToken, String code) {
        UserPhoneNumberDto userPhoneNumberDto = new UserPhoneNumberDto();
        userPhoneNumberDto.setCode(code);
        WechatPhoneResponse wechatPhoneResponse = wechatClient.getPhoneNumber(accessToken, userPhoneNumberDto);
        log.debug("wechatPhoneResponse error code: {}", wechatPhoneResponse.getErrCode());
        log.debug("wechatPhoneResponse error msg: {}", wechatPhoneResponse.getErrMsg());
        log.debug("wechatPhoneResponse phone Info: {}", wechatPhoneResponse.getPhoneInfo());
        WechatPhoneResponse.PhoneInfo phoneInfo = wechatPhoneResponse.getPhoneInfo();
        if (phoneInfo != null) {
            return phoneInfo.getPurePhoneNumber();
        }
        return null;
    }

    @Override
    public TokenDto loginWithWechat(WechatAuthDto wechatAuthDto) {
        // 获取access_token
        WechatGetAccessTokenResponse clientCredential = wechatClient.getAccessToken(wechatAppId, wechatAppSecret,
                "client_credential");
        String accessToken = clientCredential.getAccessToken();
        log.debug("accessToken: {}", accessToken);

        WechatLoginResponse wechatLogin = wechatClient
                .wechatLogin(wechatAppId, wechatAppSecret, wechatAuthDto.getCode(), "authorization_code");
        String openId = wechatLogin.getOpenId();
        String unionId = wechatLogin.getUnionId();

        if (ObjectUtils.isEmpty(openId) && ObjectUtils.isEmpty(unionId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }

        log.debug("openId: {}", openId);
        log.debug("unionId: {}", unionId);

        User user = findUserByWxUnionId(unionId);

        if (user == null) {
            user = findUserByWxOpenId(openId);
            if (user != null) {
                user.setUnionId(unionId);
                userMapper.updateById(user);
            }
        }

        if (ObjectUtils.isEmpty(user)) {
            User newUser = User.builder()
                    .openId(openId)
                    .unionId(unionId)
                    .username(UUID.randomUUID().toString())
                    .nickname(wechatAuthDto.getNickname())
                    .avatarImgUrl(wechatAuthDto.getAvatarUrl())
                    .build();

            if (wechatAuthDto.getIsPhoneLogin() != null && wechatAuthDto.getIsPhoneLogin()) {
                String phoneNumber = getPhoneNumber(accessToken, wechatAuthDto.getCode());
                newUser.setPhone(phoneNumber);
            }

            registerWithWechat(newUser);
            return getTokenDto(0L, newUser, AuthType.WECHAT);
        } else {
            if (!user.getActive()) {
                throw new BusinessException(ErrorCode.USER_NOT_ACTIVE);
            }
            boolean isChanged = false;
            if (wechatAuthDto.getIsPhoneLogin() != null && wechatAuthDto.getIsPhoneLogin() && user.getPhone() == null) {
                String phoneNumber = getPhoneNumber(accessToken, wechatAuthDto.getCode());
                user.setPhone(phoneNumber);
                isChanged = true;
            }

            if (user.getNickname() == null && wechatAuthDto.getNickname() != null) {
                user.setNickname(wechatAuthDto.getNickname());
                isChanged = true;
            }

            if (user.getAvatarImgUrl() == null && wechatAuthDto.getAvatarUrl() != null) {
                user.setAvatarImgUrl(wechatAuthDto.getAvatarUrl());
                isChanged = true;
            }

            if (user.getUsername() == null) {
                user.setUsername(UUID.randomUUID().toString());
                isChanged = true;
            }

            if (isChanged) {
                userMapper.updateById(user);
            }

            Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistory>lambdaQuery()
                    .eq(AuthHistory::getWechatId, unionId));
            return getTokenDto(authCount, user, AuthType.WECHAT);
        }
    }

    @Override
    public TokenDto zmkm(String ud) {
        User user = findUserByWxUnionId(ud);
        Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistory>lambdaQuery()
                .eq(AuthHistory::getWechatId, ud));
        return getTokenDto(authCount, user, AuthType.WECHAT);
    }

    @Override
    public TokenDto guestLogin(String deviceId) {
        List<User> userEntities = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getDeviceId, deviceId)
                .eq(User::getRole, RoleEnum.GUEST));
        User user;
        if (!ObjectUtils.isEmpty(userEntities)) {
            user = userEntities.get(0);
        } else {
            user = User.builder()
                    .role(RoleEnum.GUEST)
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
                .role(RoleEnum.NORMAL_USER)
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
                .role(RoleEnum.NORMAL_USER)
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
    public IPage<User> findUsers(Page<User> page, String username, String nickname) {
        return userMapper.selectPage(page, Wrappers.<User>lambdaQuery()
                .eq(!ObjectUtils.isEmpty(username), User::getUsername, username)
                .like(!ObjectUtils.isEmpty(nickname), User::getNickname, nickname)
                .orderByDesc(User::getCreatedAt));
    }

    @Override
    public User findUser(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public void changeUserStatus(String id, Boolean active) {
        User user = userMapper.selectById(id);
        user.setActive(active);
        userMapper.updateById(user);
    }

    @Override
    public void sendRegisterVerificationEmail(String email) {
        String verifyCode = generateVerifyCode();
        String htmlBody = "<h1>NovlNovl Register Verification Code</h1>"
                + "<p>Your verification code is:<p>"
                + "<h2>" + verifyCode + "</h2>"
                + "<p>Please filled in the code into your App, it's only valid in "
                + "<red>" + EmailBusinessServiceImpl.VERIFICATION_CODE_VALID_SECONDS + "</red> seconds.</p>";
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("NovlNovl - Email Address Register Verification");
        emailDto.setCode(verifyCode);
        emailDto.setText(htmlBody);
        emailDto.setEmailType(EmailType.REGISTER);
        log.info(emailDto.getCode());
        emailBusinessService.sendSimpleMessage(emailDto);
    }

    @Override
    public void sendForgetPasswordVerificationEmail(String email) {
        String verifyCode = generateVerifyCode();
        String htmlBody = "<h1>NovlNovl Reset Password Verification Code</h1>"
                + "<p>Your verification code is:<p>"
                + "<h2>" + verifyCode + "</h2>"
                + "<p>Please filled in the code into your App, it's only valid in "
                + "<red>" + EmailBusinessServiceImpl.VERIFICATION_CODE_VALID_SECONDS + "</red> seconds.</p>";
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("NovlNovl - Reset Password Verification");
        emailDto.setCode(verifyCode);
        emailDto.setText(htmlBody);
        emailDto.setEmailType(EmailType.FORGET);
        log.info(emailDto.getCode());
        emailBusinessService.sendSimpleMessage(emailDto);
    }

    @Override
    public void sendLinkVerificationEmail(String email) {
        String verifyCode = generateVerifyCode();
        String htmlBody = "<h1>NovlNovl Link Email Verification Code</h1>"
                + "<p>Your verification code is:<p>"
                + "<h2>" + verifyCode + "</h2>"
                + "<p>Please filled in the code into your App, it's only valid in "
                + "<red>" + EmailBusinessServiceImpl.VERIFICATION_CODE_VALID_SECONDS + "</red> seconds.</p>";
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(email);
        emailDto.setSubject("NovlNovl - Link Email Address Verification");
        emailDto.setCode(verifyCode);
        emailDto.setText(htmlBody);
        emailDto.setEmailType(EmailType.LINK);
        log.info(emailDto.getCode());
        emailBusinessService.sendSimpleMessage(emailDto);
    }

    @Override
    public void sendLoginVerificationSms(String phone) {
        String verifyCode = generateVerifyCode();
        smsService.sendSms(SmsType.VERIFICATION, phone, verifyCode);
    }

    @Override
    public UserDto getUserAndChildAgeInfoByUsername(String username) {
        return userMapper.getUserAndChildAgeInfoByUsername(username);
    }

    @Override
    public User assignOpenId(WechatAuthDto wechatAuthDto, User user) {
        WechatLoginResponse wechatLogin = wechatClient
                .wechatLogin(wechatAppId, wechatAppSecret, wechatAuthDto.getCode(), "authorization_code");
        user.setOpenId(wechatLogin.getOpenId());
        userMapper.updateById(user);
        return user;
    }

}
