package net.stepbooks.domain.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.domain.admin.mapper.AdminUserMapper;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.security.admin.AdminJwtTokenProvider;
import net.stepbooks.interfaces.admin.assembler.AdminAuthAssembler;
import net.stepbooks.interfaces.client.dto.TokenDto;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserMapper adminUserMapper;
    private final AdminJwtTokenProvider adminJwtTokenProvider;

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
        JwtUserDetails userDetails = JwtUserDetails.builder().email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .nickname(userEntity.getNickname())
                .build();
        return adminJwtTokenProvider.refreshToken(userDetails, refreshToken, userEntity.getModifiedAt());
    }

    @Override
    public void updateUserById(String id, AdminUser updatedUser) {
        AdminUser userEntity = adminUserMapper.selectById(id);
        BeanUtils.copyProperties(updatedUser, userEntity);
        adminUserMapper.updateById(userEntity);
    }


}
