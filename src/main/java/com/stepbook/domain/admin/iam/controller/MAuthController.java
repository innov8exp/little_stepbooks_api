package com.stepbook.domain.admin.iam.controller;

import com.stepbook.domain.admin.iam.dto.AdminUserDto;
import com.stepbook.domain.admin.iam.dto.LoginDto;
import com.stepbook.domain.admin.iam.entity.AdminUserEntity;
import com.stepbook.domain.admin.iam.service.AdminUserService;
import com.stepbook.domain.user.dto.TokenDto;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import com.stepbook.infrastructure.exception.BusinessException;
import com.stepbook.infrastructure.exception.ErrorCode;
import com.stepbook.infrastructure.model.JwtUserDetails;
import com.stepbook.infrastructure.security.admin.AdminJwtTokenProvider;
import com.stepbook.infrastructure.util.JsonUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/admin/auth")
public class MAuthController {

    private final AdminUserService adminUserService;
    private final AdminJwtTokenProvider adminJwtTokenProvider;
    @Value("${admin.jwt.access-token.cookie-name}")
    private String accessTokenCookieName;
    @Value("${admin.jwt.refresh-token.cookie-name}")
    private String refreshTokenCookieName;
    @Value("${admin.jwt.token-type}")
    private String tokenType;
    @Value("${admin.jwt.header}")
    private String authHeader;

    public MAuthController(AdminUserService adminUserService,
                           AdminJwtTokenProvider adminJwtTokenProvider) {
        this.adminUserService = adminUserService;
        this.adminJwtTokenProvider = adminJwtTokenProvider;
    }

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
            if (accessToken != null && refreshToken != null) {
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
        if (accessToken == null) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "cannot found access_token from cookie");
        }
        var username = adminJwtTokenProvider.getSubjectFromToken(accessToken);
        var userDto = adminUserService.findUserByUsername(username);
        if (userDto == null) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "无效的登录信息，请重新登录");
        }
        JwtUserDetails userDetails = JwtUserDetails.builder().email(userDto.getEmail()).username(username).build();
        TokenDto tokenDto = adminJwtTokenProvider.refreshToken(userDetails, refreshToken, userDto.getModifiedAt());
        return setCookie(tokenDto);
    }

    @GetMapping("/user-info")
    public ResponseEntity<AdminUserDto> userInfo() {
        JwtUserDetails details = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AdminUserEntity adminUserEntity = adminUserService.findUserByUsername(details.getUsername());
        AdminUserDto userDto = BaseAssembler.convert(adminUserEntity, AdminUserDto.class);
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
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
//        Mono<APIResponse> apiResponseMono = adminUserService.resetPassword(resetPasswordDto);
//        return apiResponseMono.flatMap(apiResponse -> Mono.just(ResponseEntity.ok().build()));
//    }

    private ResponseEntity<TokenDto> setCookie(TokenDto token) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(accessTokenCookieName, token.getAccessToken())
//                .sameSite("Strict")
                .httpOnly(true)
                .secure(false)  // TODO for production env should be true
                .maxAge(token.getRefreshExpiresIn())
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(refreshTokenCookieName, token.getRefreshToken())
//                .sameSite("Strict")
                .httpOnly(true)
                .secure(false) // TODO for production env should be true
                .maxAge(token.getRefreshExpiresIn())
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(token);
    }
}
