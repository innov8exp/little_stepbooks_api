package net.stepbooks.interfaces.admin.controller.v1;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.security.admin.AdminJwtTokenProvider;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.AdminUserDto;
import net.stepbooks.interfaces.admin.dto.AdminUserResetPasswordDto;
import net.stepbooks.interfaces.admin.dto.LoginDto;
import net.stepbooks.interfaces.admin.dto.ResetPasswordDto;
import net.stepbooks.interfaces.client.dto.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/auth")
public class MAuthController {

    private final AdminUserService adminUserService;
    private final AdminJwtTokenProvider adminJwtTokenProvider;
    private final ContextManager contextManager;
    @Value("${admin.jwt.access-token.cookie-name}")
    private String accessTokenCookieName;
    @Value("${admin.jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;
    @Value("${admin.jwt.token-type}")
    private String tokenType;
    @Value("${admin.jwt.header}")
    private String authHeader;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authToken(@Valid @RequestBody LoginDto loginDto) {
        TokenDto tokenDto = adminUserService.loginWithEmail(loginDto.getEmail(), loginDto.getPassword());
        return setCookie(tokenDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshToken(HttpServletRequest request) {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "cannot found access_token from cookie");
        }
        for (Cookie cookie : cookies) {
            if (!ObjectUtils.isEmpty(accessToken) && !ObjectUtils.isEmpty(refreshToken)) {
                break;
            }
            if (accessTokenCookieName.equals(cookie.getName())) {
                accessToken = cookie.getValue();
                continue;
            }
            if (refreshTokenCookieName.equals(cookie.getName())) {
                refreshToken = cookie.getValue();
            }
        }
        if (ObjectUtils.isEmpty(accessToken)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "cannot found access_token from cookie");
        }
        var username = adminJwtTokenProvider.getSubjectFromToken(accessToken);
        var userDto = adminUserService.findUserByUsername(username);
        if (ObjectUtils.isEmpty(userDto)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "无效的登录信息，请重新登录");
        }
        JwtUserDetails userDetails = JwtUserDetails.builder().email(userDto.getEmail()).username(username).build();
        TokenDto tokenDto = adminJwtTokenProvider.refreshToken(userDetails, refreshToken, userDto.getModifiedAt());
        return setCookie(tokenDto);
    }

    @GetMapping("/user-info")
    public ResponseEntity<AdminUserDto> userInfo() {
        AdminUser adminUser = contextManager.currentAdminUser();
        AdminUserDto userDto = BaseAssembler.convert(adminUser, AdminUserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<TokenDto> logout() {
        var token = TokenDto.builder()
                .accessToken(null)
                .refreshToken(null)
                .refreshExpiresIn(0L)
                .expiresIn(0L)
                .build();
        return setCookie(token);
    }

    @PostMapping("/sms/code")
    public ResponseEntity<?> codeValidation(@RequestBody @Valid AdminUserResetPasswordDto adminUserResetPasswordDto) {
        String phone = adminUserResetPasswordDto.getPhone();
        adminUserService.findUserByPhone(phone);
        adminUserService.sendLoginVerificationSms(phone);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/user-info")
//    public ResponseEntity<?> updateCurrentUserInfo(@Valid @RequestBody AdminUserDto userDto) {
//        AdminUserDto currentAdminUser = ContextUtil.currentAdminUser();
//        AdminUserEntity userEntity = adminUserService.findUserByEmail(currentAdminUser.getEmail());
//        UpdateAdminUserEntity updateAdminUserEntity = UpdateAdminUserEntity.builder().build();
//        BeanUtils.copyProperties(userDto, userEntity, "userRole");
//        BeanUtils.copyProperties(userEntity, updateAdminUserEntity);
//        return adminUserService.updateUser(updateAdminUserEntity)
//                .flatMap(user -> Mono.just(ResponseEntity.ok().build()));
//    }
//
//    @PutMapping("/password")
//    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
//        AdminUserDto userDto = ContextUtil.currentAdminUser();
//        updatePasswordDto.setEmail(userDto.getEmail());
//        Mono<APIResponse> apiResponseMono = adminUserService.updatePassword(updatePasswordDto);
//        return apiResponseMono.flatMap(apiResponse -> Mono.just(ResponseEntity.ok().build()));
//    }
//
//    @PostMapping("/forget-password")
//    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {
//        Mono<APIResponse> apiResponseMono = adminUserService.forgetPassword(forgetPasswordDto.getEmail());
//        return apiResponseMono.flatMap(apiResponse -> Mono.just(ResponseEntity.ok().build()));
//    }
//
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        adminUserService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<TokenDto> setCookie(TokenDto token) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(accessTokenCookieName, token.getAccessToken())
//                .sameSite("Strict")
                .httpOnly(true)
                .secure(true)  // TODO for production env should be true
                .maxAge(token.getRefreshExpiresIn())
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(refreshTokenCookieName, token.getRefreshToken())
//                .sameSite("Strict")
                .httpOnly(true)
                .secure(true) // TODO for production env should be true
                .maxAge(token.getRefreshExpiresIn())
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(token);
    }
}
