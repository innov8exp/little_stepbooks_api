package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.client.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "Auth", description = "认证相关接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.token-type}")
    private String tokenType;
    @Value("${jwt.header}")
    private String authHeader;

    private final UserService userService;
    private final EmailService emailService;
    private final ContextManager contextManager;

    @PostMapping("/login")
    @Operation(summary = "使用邮箱和密码登录")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = userService.loginWithEmail(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "使用微信登录")
    @PostMapping("/wechat-login")
    public ResponseEntity<TokenDto> wechatLogin(@Valid @RequestBody WechatAuthDto wechatAuthDto) {
        TokenDto tokenDto = userService.loginWithWechat(wechatAuthDto);
        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "绑定微信openid")
    @PostMapping("/openid")
    public ResponseEntity<?> bindOpenId(@Valid @RequestBody WechatAuthDto wechatAuthDto) {
        User user = contextManager.currentUser();
        if (ObjectUtils.isEmpty(user.getOpenId())) {
            userService.assignOpenId(wechatAuthDto, user);
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/social-login")
    @Operation(summary = "使用第三方登录")
    public ResponseEntity<TokenDto> socialLogin(@Valid @RequestBody SocialAuthDto socialAuthDto) {
        TokenDto tokenDto = userService.socialLogin(socialAuthDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/guest-login")
    @Operation(summary = "游客登录")
    public ResponseEntity<TokenDto> guestLogin(@Valid @RequestBody GuestAuthDto guestAuthDto) {
        TokenDto tokenDto = userService.guestLogin(guestAuthDto.getDeviceId());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/verification")
    @Operation(summary = "发送验证码")
    public ResponseEntity<?> sendVerificationCode(@Valid @RequestBody VerificationDto verificationDto) {
        switch (verificationDto.getEmailType()) {
            case "REGISTER":
                if (userService.existsUserByEmail(verificationDto.getEmail())) {
                    throw new BusinessException(ErrorCode.EMAIL_EXISTS_ERROR);
                }
                userService.sendRegisterVerificationEmail(verificationDto.getEmail());
                break;
            case "FORGET":
                User userByEmail = userService.findUserByEmail(verificationDto.getEmail());
                if (ObjectUtils.isEmpty(userByEmail)) {
                    throw new BusinessException(ErrorCode.USER_NOT_FOUND);
                }
                userService.sendForgetPasswordVerificationEmail(verificationDto.getEmail());
                break;
            default:
                throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validation")
    @Operation(summary = "验证验证码")
    public ResponseEntity<Boolean> validateTheVerificationCode(@Valid @RequestBody ValidateDto validateDto) {
        Boolean valid = emailService.verifyValidationCode(validateDto.getEmail(),
                validateDto.getCode(), EmailType.valueOf(validateDto.getEmailType()));
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public ResponseEntity<TokenDto> register(@Valid @RequestBody RegisterDto registerDto) {
        Boolean valid = emailService.verifyValidationCode(registerDto.getEmail(),
                registerDto.getCode(), EmailType.REGISTER);
        if (!valid) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        User user = BaseAssembler.convert(registerDto, User.class);
        user.setId(null);
        userService.registerWithEmail(user);
        TokenDto tokenDto = userService.loginWithEmail(registerDto.getEmail(), registerDto.getPassword());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "重置密码")
    public ResponseEntity<TokenDto> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        Boolean valid = emailService.verifyValidationCode(resetPasswordDto.getEmail(),
                resetPasswordDto.getResetToken(), EmailType.FORGET);
        if (!valid) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        User user = userService.findUserByEmail(resetPasswordDto.getEmail());
        user.setPassword(resetPasswordDto.getNewPassword());
        userService.resetPassword(user);
        TokenDto tokenDto = userService.loginWithEmail(resetPasswordDto.getEmail(), resetPasswordDto.getNewPassword());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新token")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest httpRequest,
                                                 @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String accessToken = httpRequest.getHeader(authHeader);
        //按道理，未登录状态不应该调用refresh token的请求，但是为了解决客户端的报错问题，返回一个空结果
        if (accessToken == null) {
            return ResponseEntity.ok(null);
        }
        if (accessToken.startsWith(tokenType)) {
            accessToken = accessToken.substring(tokenType.length()).trim();
        }
        TokenDto tokenDto = userService.refreshToken(accessToken, refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    @SecurityRequirement(name = "Client Authentication")
    @GetMapping("/user-info")
    @Operation(summary = "获取用户信息")
    public ResponseEntity<UserDto> userInfo() {
        User user = contextManager.currentUser();
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        UserDto userInfo = userService.getUserAndChildAgeInfoByUsername(user.getUsername());
        return ResponseEntity.ok(userInfo);
    }
}
