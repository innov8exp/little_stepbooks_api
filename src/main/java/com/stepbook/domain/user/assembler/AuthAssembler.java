package com.stepbook.domain.user.assembler;

import com.stepbook.domain.user.entity.UserEntity;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import com.stepbook.infrastructure.model.JwtUserDetails;

public class AuthAssembler extends BaseAssembler {

    public static JwtUserDetails userEntityToJwtUserDetails(UserEntity userEntity) {
        return JwtUserDetails.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .googleId(userEntity.getGoogleId())
                .facebookId(userEntity.getFacebookId())
                .build();
    }

    public static JwtUserDetails guestEntityToJwtUserDetails(String deviceId) {
        return JwtUserDetails.builder()
                .username(deviceId)
                .build();
    }

}
