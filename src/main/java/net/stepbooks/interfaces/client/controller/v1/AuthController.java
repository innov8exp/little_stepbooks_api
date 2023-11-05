package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.stepbooks.application.dto.client.*;
import net.stepbooks.domain.email.service.EmailService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.domain.user.service.UserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = userService.loginWithEmail(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/social-login")
    public ResponseEntity<TokenDto> socialLogin(@Valid @RequestBody SocialAuthDto socialAuthDto) {
        TokenDto tokenDto = userService.socialLogin(socialAuthDto);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/guest-login")
    public ResponseEntity<TokenDto> guestLogin(@Valid @RequestBody GuestAuthDto guestAuthDto) {
        TokenDto tokenDto = userService.guestLogin(guestAuthDto.getDeviceId());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/verification")
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
    public ResponseEntity<Boolean> validateTheVerificationCode(@Valid @RequestBody ValidateDto validateDto) {
        Boolean valid = emailService.verifyValidationCode(validateDto.getEmail(),
                validateDto.getCode(), EmailType.valueOf(validateDto.getEmailType()));
        return ResponseEntity.ok(valid);
    }

    @PostMapping("/register")
    @ApiResponse(responseCode = "201")
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
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(tokenDto);
    }

    @PostMapping("/reset-password")
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
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest httpRequest,
                                                 @Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        String accessToken = httpRequest.getHeader(authHeader);
        if (accessToken.startsWith(tokenType)) {
            accessToken = accessToken.substring(tokenType.length()).trim();
        }
        TokenDto tokenDto = userService.refreshToken(accessToken, refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDto> userInfo() {
        JwtUserDetails details = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userByEmail = userService.findUserByUsername(details.getUsername());
        UserDto userDto = BaseAssembler.convert(userByEmail, UserDto.class);
        return ResponseEntity.ok(userDto);
    }

//    @PostMapping("/guest-token")
//    public ResponseEntity<TokenDto> createGuestToken() {
//        TokenDto tokenDto = userService.loginWithEmail(loginDto.getEmail(), loginDto.getPassword());
//        return ResponseEntity.ok(tokenDto);
//    }
}
