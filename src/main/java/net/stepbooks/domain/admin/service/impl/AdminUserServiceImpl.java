package net.stepbooks.domain.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.admin.mapper.AdminUserMapper;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.domain.sms.entity.Sms;
import net.stepbooks.domain.sms.service.SmsService;
import net.stepbooks.infrastructure.enums.SmsType;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.security.admin.AdminJwtTokenProvider;
import net.stepbooks.infrastructure.util.EncryptUtils;
import net.stepbooks.interfaces.admin.assembler.AdminAuthAssembler;
import net.stepbooks.interfaces.admin.dto.ResetPasswordDto;
import net.stepbooks.interfaces.client.dto.TokenDto;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final AdminJwtTokenProvider adminJwtTokenProvider;
    private final SmsService smsService;

    @Override
    public AdminUser findUserByEmail(String email) {
        AdminUser userEntity = adminUserMapper.selectOne(Wrappers.<AdminUser>lambdaQuery()
                .eq(AdminUser::getEmail, email));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with email: " + email);
        }
        return userEntity;
    }

    @Override
    public AdminUser findUserByPhone(String phone) {
        AdminUser user = adminUserMapper.selectOne(Wrappers.<AdminUser>lambdaQuery().eq(AdminUser::getPhone, phone));
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with phone: " + phone);
        }
        return user;
    }

    @Override
    public AdminUser findUserByUsername(String username) {
        AdminUser userEntity = adminUserMapper.selectOne(Wrappers.<AdminUser>lambdaQuery()
                .eq(AdminUser::getUsername, username));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with username: " + username);
        }
        return userEntity;
    }

    @Override
    public void registerWithEmail(AdminUser userEntity) {
        String email = userEntity.getEmail();
        AdminUser isUserExist = adminUserMapper.selectOne(Wrappers.<AdminUser>lambdaQuery()
                .eq(AdminUser::getEmail, email));
        if (!ObjectUtils.isEmpty(isUserExist)) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS_ERROR,
                    "Email: " + email + " has been registered, please select another one.");
        }
        String password = PasswordEncoderFactories.createDelegatingPasswordEncoder()
                .encode(userEntity.getPassword());
        userEntity.setPassword(password);
        int insert = adminUserMapper.insert(userEntity);
        if (insert != 1) {
            throw new BusinessException(ErrorCode.DATABASE_OPERATOR_ERROR, "Create user error");
        }
    }

    @Override
    public TokenDto loginWithEmail(String email, String password) {
        AdminUser userEntity = this.findUserByEmail(email);
        var valid = PasswordEncoderFactories.createDelegatingPasswordEncoder()
                .matches(password, userEntity.getPassword());
        if (!valid) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "auth failed, please input the correct password");
        }
        JwtUserDetails jwtUserDetails = AdminAuthAssembler.adminUserEntityToJwtUserDetails(userEntity);
        return adminJwtTokenProvider.generateToken(jwtUserDetails);
    }

    @Override
    public AdminUser getUserInfoFromToken(String token) {
        return null;
    }

    @Override
    public TokenDto refreshToken(String accessToken, String refreshToken) {
        String username = adminJwtTokenProvider.getSubjectFromToken(accessToken);
        AdminUser userEntity = this.findUserByUsername(username);
        if (userEntity == null) {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        JwtUserDetails userDetails = AdminAuthAssembler.adminUserEntityToJwtUserDetails(userEntity);
        return adminJwtTokenProvider.refreshToken(userDetails, refreshToken, userEntity.getModifiedAt());
    }

    @Override
    public void updateUserById(String id, AdminUser updatedUser) {
        AdminUser userEntity = adminUserMapper.selectById(id);
        BeanUtils.copyProperties(updatedUser, userEntity);
        adminUserMapper.updateById(userEntity);
    }

    @Override
    public void sendLoginVerificationSms(String phone) {
        String verifyCode = generateVerifyCode();
        smsService.sendSms(SmsType.VERIFICATION, phone, verifyCode);
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        boolean exists = smsService.exists(Wrappers.<Sms>lambdaQuery().eq(Sms::getPhone, resetPasswordDto.getPhone())
                .eq(Sms::getContent, resetPasswordDto.getVerifyCode())
                .eq(Sms::getSmsType, SmsType.VERIFICATION)
        );
        if (!exists) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Invalid verify code");
        }
        AdminUser adminUser = findUserByPhone(resetPasswordDto.getPhone());
        String password = PasswordEncoderFactories.createDelegatingPasswordEncoder()
                .encode(adminUser.getPassword());
        adminUser.setPassword(password);
        adminUserMapper.updateById(adminUser);
    }

    @Override
    public void changePassword(String id, String oldMd5, String newMd5) {
        AdminUser adminUser = adminUserMapper.selectById(id);
        if (EncryptUtils.matches(oldMd5, adminUser.getPassword())) {
            String password = EncryptUtils.encodePassword(newMd5);
            adminUser.setPassword(password);
            adminUserMapper.updateById(adminUser);
        } else {
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    private static String generateVerifyCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }
}
