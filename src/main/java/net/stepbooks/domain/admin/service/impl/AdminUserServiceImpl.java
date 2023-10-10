package net.stepbooks.domain.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.stepbooks.domain.admin.service.AdminUserService;
import net.stepbooks.interfaces.admin.assembler.AdminAuthAssembler;
import net.stepbooks.domain.admin.entity.AdminUserEntity;
import net.stepbooks.interfaces.client.dto.TokenDto;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.domain.admin.mapper.AdminUserMapper;
import net.stepbooks.infrastructure.model.JwtUserDetails;
import net.stepbooks.infrastructure.security.admin.AdminJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public AdminUserEntity findUserByEmail(String email) {
        AdminUserEntity userEntity = adminUserMapper.selectOne(Wrappers.<AdminUserEntity>lambdaQuery()
                .eq(AdminUserEntity::getEmail, email));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with email: " + email);
        }
        return userEntity;
    }

    @Override
    public AdminUserEntity findUserByUsername(String username) {
        AdminUserEntity userEntity = adminUserMapper.selectOne(Wrappers.<AdminUserEntity>lambdaQuery()
                .eq(AdminUserEntity::getUsername, username));
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "Cannot found the user with username: " + username);
        }
        return userEntity;
    }

    @Override
    public void registerWithEmail(AdminUserEntity userEntity) {
        String email = userEntity.getEmail();
        AdminUserEntity isUserExist = adminUserMapper.selectOne(Wrappers.<AdminUserEntity>lambdaQuery()
                .eq(AdminUserEntity::getEmail, email));
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
        AdminUserEntity userEntity = this.findUserByEmail(email);
        var valid = PasswordEncoderFactories.createDelegatingPasswordEncoder()
                .matches(password, userEntity.getPassword());
        if (!valid) {
            throw new BusinessException(ErrorCode.AUTH_ERROR, "auth failed, please input the correct password");
        }
        JwtUserDetails jwtUserDetails = AdminAuthAssembler.adminUserEntityToJwtUserDetails(userEntity);
        return adminJwtTokenProvider.generateToken(jwtUserDetails);
    }

    @Override
    public AdminUserEntity getUserInfoFromToken(String token) {
        return null;
    }

    @Override
    public TokenDto refreshToken(String accessToken, String refreshToken) {
        String username = adminJwtTokenProvider.getSubjectFromToken(accessToken);
        AdminUserEntity userEntity = this.findUserByUsername(username);
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
    public void updateUserById(String id, AdminUserEntity updatedUser) {
        AdminUserEntity userEntity = adminUserMapper.selectById(id);
        BeanUtils.copyProperties(updatedUser, userEntity);
        adminUserMapper.updateById(userEntity);
    }


}
