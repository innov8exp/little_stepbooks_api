package net.stepbooks.domain.user.service.impl;

import net.stepbooks.interfaces.client.dto.*;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.domain.email.service.impl.EmailServiceImpl;
import net.stepbooks.domain.common.service.FileService;
import net.stepbooks.interfaces.client.assembler.AuthAssembler;
import net.stepbooks.application.client.FacebookClient;
import net.stepbooks.application.client.GoogleClient;
import net.stepbooks.domain.user.entity.AuthHistoryEntity;
import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.domain.user.entity.UserTagRefEntity;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.enums.AuthType;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.enums.RoleEnum;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.domain.user.mapper.AuthHistoryMapper;
import net.stepbooks.domain.user.mapper.UserMapper;
import net.stepbooks.domain.user.mapper.UserTagRefMapper;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.security.user.UserJwtTokenProvider;
import net.stepbooks.infrastructure.util.CommonUtil;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserTagRefMapper userTagRefMapper;
    private final FileService fileService;
    private final FacebookClient facebookClient;
    private final GoogleClient googleClient;

    @Value("${aws.cdn}")
    private String cdnUrl;
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
        Long count = userMapper.selectCount(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getEmail, email));
        return count > 0;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        UserEntity userEntity = userMapper.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getEmail, email));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with email: " + email);
        }
        return userEntity;
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        UserEntity userEntity = userMapper.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getUsername, username));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with username: " + username);
        }
        return userEntity;
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Transactional
    @Override
    public void registerWithEmail(UserEntity userEntity) {
        String email = userEntity.getEmail();
        UserEntity isUserExist = userMapper.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getEmail, email));
        if (!ObjectUtils.isEmpty(isUserExist)) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS_ERROR,
                    "Email: " + email + " has been registered, please select another one.");
        }
        String password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userEntity.getPassword());
        userEntity.setPassword(password);
        userEntity.setRole(RoleEnum.NORMAL_USER.getValue());
        userEntity.setNickname("Novl" + CommonUtil.getStringRandom(8));
        userEntity.setUsername(UUID.randomUUID().toString());
        int insert = userMapper.insert(userEntity);
        AuthHistoryEntity authHistoryEntity = new AuthHistoryEntity();
        authHistoryEntity.setUsername(userEntity.getUsername());
        authHistoryEntity.setAuthType(AuthType.EMAIL);
        authHistoryEntity.setEmail(email);
        authHistoryEntity.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistoryEntity);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
    }

    @Transactional
    @Override
    public void resetPassword(UserEntity userEntity) {
        String email = userEntity.getEmail();
        String password = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(userEntity.getPassword());
        userEntity.setPassword(password);
        int update = userMapper.updateById(userEntity);
        AuthHistoryEntity authHistoryEntity = new AuthHistoryEntity();
        authHistoryEntity.setAuthType(AuthType.EMAIL);
        authHistoryEntity.setUsername(userEntity.getUsername());
        authHistoryEntity.setEmail(email);
        authHistoryEntity.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistoryEntity);
        if (update != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Reset password error");
        }
    }

    @Override
    public TokenDto loginWithEmail(String email, String password) {
        UserEntity userEntity = this.findUserByEmail(email);
        var valid = PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(password, userEntity.getPassword());
        if (!valid) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "auth failed, please input the correct password");
        }
        Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistoryEntity>lambdaQuery()
                .eq(AuthHistoryEntity::getEmail, email));
        return getTokenDto(authCount, userEntity, AuthType.EMAIL);
    }

    @Override
    public TokenDto guestLogin(String deviceId) {
        List<UserEntity> userEntities = userMapper.selectList(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getDeviceId, deviceId)
                .eq(UserEntity::getRole, RoleEnum.GUEST.getValue()));
        UserEntity userEntity;
        if (!ObjectUtils.isEmpty(userEntities)) {
            userEntity = userEntities.get(0);
        } else {
            userEntity = UserEntity.builder()
                    .role(RoleEnum.GUEST.getValue())
                    .deviceId(deviceId)
                    .username(UUID.randomUUID().toString())
                    .nickname("Guest User").build();
            int insert = userMapper.insert(userEntity);
            if (insert != 1) {
                throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
            }
        }
        return getTokenDto(0L, userEntity, AuthType.GUEST);
    }

    private TokenDto getTokenDto(Long authCount, UserEntity userEntity, AuthType authType) {
        JwtUserDetails jwtUserDetails = AuthAssembler.userEntityToJwtUserDetails(userEntity);
        TokenDto tokenDto = jwtTokenProvider.generateToken(jwtUserDetails, authType);
        // First time to login
        tokenDto.setIsFirstAuth(authCount == 0);
        tokenDto.setRole(userEntity.getRole());
        AuthHistoryEntity authHistoryEntity = new AuthHistoryEntity();
        authHistoryEntity.setUsername(userEntity.getUsername());
        authHistoryEntity.setAuthType(authType);
        authHistoryEntity.setEmail(userEntity.getEmail());
        authHistoryEntity.setFacebookId(userEntity.getFacebookId());
        authHistoryEntity.setGoogleId(userEntity.getGoogleId());
        authHistoryEntity.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistoryEntity);
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
                UserEntity userEntity = this.findUserByFacebookId(facebookId);
                if (userEntity != null) {
                    Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistoryEntity>lambdaQuery()
                            .eq(AuthHistoryEntity::getFacebookId, facebookId));
                    return getTokenDto(authCount, userEntity, AuthType.FACEBOOK);
                } else {
                    FacebookUserDto facebookUserInfo = getFacebookUserInfo(facebookId, accessToken);
                    userEntity = registerUserFromFacebook(socialAuthDto.getDeviceId(), facebookId, facebookUserInfo);
                    JwtUserDetails jwtUserDetails = AuthAssembler.userEntityToJwtUserDetails(userEntity);
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
                UserEntity userEntity = this.findUserByGoogleId(googleId);
                if (userEntity != null) {
                    Long authCount = authHistoryMapper.selectCount(Wrappers.<AuthHistoryEntity>lambdaQuery()
                            .eq(AuthHistoryEntity::getGoogleId, googleId));
                    return getTokenDto(authCount, userEntity, AuthType.GOOGLE);
                } else {
                    userEntity = registerUserFromGoogle(socialAuthDto.getDeviceId(), googleId, googleUserDto);
                    JwtUserDetails jwtUserDetails = AuthAssembler.userEntityToJwtUserDetails(userEntity);
                    TokenDto token = jwtTokenProvider.generateToken(jwtUserDetails, AuthType.GOOGLE);
                    token.setIsFirstAuth(true);
                    return token;
                }
            }
        }
        throw new BusinessException(ErrorCode.AUTH_ERROR, "Failed auth with facebook");
    }

    private UserEntity registerUserFromFacebook(String deviceId, String facebookId, FacebookUserDto facebookUserInfo) {
        UserEntity userEntity = UserEntity.builder()
                .role(RoleEnum.NORMAL_USER.getValue())
                .username(UUID.randomUUID().toString())
                .nickname(facebookUserInfo.getName())
                .facebookId(facebookId)
                .avatarImg(facebookUserInfo.getPicture().getData().getUrl())
                .deviceId(deviceId)
                .build();
        int insert = userMapper.insert(userEntity);
        AuthHistoryEntity authHistoryEntity = new AuthHistoryEntity();
        authHistoryEntity.setUsername(userEntity.getUsername());
        authHistoryEntity.setAuthType(AuthType.FACEBOOK);
        authHistoryEntity.setFacebookId(facebookId);
        authHistoryEntity.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistoryEntity);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
        return userEntity;
    }

    private UserEntity registerUserFromGoogle(String deviceId, String googleId, GoogleUserDto googleUserDto) {
        UserEntity userEntity = UserEntity.builder()
                .role(RoleEnum.NORMAL_USER.getValue())
                .username(UUID.randomUUID().toString())
                .nickname(googleUserDto.getName())
//                .email(googleUserDto.getEmail())
                .googleId(googleId)
                .avatarImg(googleUserDto.getPicture())
                .deviceId(deviceId)
                .build();
        int insert = userMapper.insert(userEntity);
        AuthHistoryEntity authHistoryEntity = new AuthHistoryEntity();
        authHistoryEntity.setUsername(userEntity.getUsername());
        authHistoryEntity.setAuthType(AuthType.GOOGLE);
        authHistoryEntity.setGoogleId(googleId);
        authHistoryEntity.setCreatedAt(LocalDateTime.now());
        authHistoryMapper.insert(authHistoryEntity);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
        return userEntity;
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
    public UserEntity findUserByFacebookId(String facebookUserId) {
        return userMapper.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getFacebookId, facebookUserId));
    }

    @Override
    public UserEntity findUserByGoogleId(String googleUserId) {
        return userMapper.selectOne(Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getGoogleId, googleUserId));
    }

    @Override
    public UserEntity getUserInfoFromToken(String token) {
        return null;
    }

    @Override
    public TokenDto refreshToken(String accessToken, String refreshToken) {
        String username = jwtTokenProvider.getSubjectFromToken(accessToken);
        Map<String, Claim> claims = jwtTokenProvider.getClaimsFromToken(accessToken);
        UserEntity userEntity = this.findUserByUsername(username);
        if (userEntity == null) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        JwtUserDetails userDetails = JwtUserDetails.builder()
                .username(username)
                .email(userEntity.getUsername())
                .nickname(userEntity.getNickname())
                .googleId(userEntity.getGoogleId())
                .facebookId(userEntity.getFacebookId())
                .build();
        return jwtTokenProvider.refreshToken(userDetails, refreshToken, AuthType.valueOf(claims.get("authType").asString()),
                userEntity.getModifiedAt());
    }

    @Override
    public void updateUserById(String id, UserEntity updatedUser) {
        UserEntity userEntity = userMapper.selectById(id);
        BeanUtils.copyProperties(updatedUser, userEntity, CommonUtil.getNullPropertyNames(updatedUser));
        userMapper.updateById(userEntity);
    }

    @Override
    public List<UserEntity> findUsers() {
        return userMapper.selectList(Wrappers.emptyWrapper());
    }

    @Override
    public UserEntity findUser(String id) {
        return userMapper.selectById(id);
    }

    @Override
    public void deleteUser(String id) {
        UserEntity userEntity = userMapper.selectById(id);
        userEntity.setActive(false);
        userMapper.updateById(userEntity);
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

    @Transactional
    @Override
    public void createUserTagRef(List<UserTagRefEntity> userTagRefEntities) {
        userTagRefEntities.forEach(userTagRefMapper::insert);
    }

    @Override
    public String uploadImg(MultipartFile file, String userId) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        String imgUrl = cdnUrl + "/" + fileService.upload(file, STORE_PATH
                + userId + "_" + UUID.randomUUID() + "." + fileType);
        UserEntity userEntity = UserEntity.builder().avatarImg(imgUrl).build();
        updateUserById(userId, userEntity);
        return imgUrl;
    }

}
