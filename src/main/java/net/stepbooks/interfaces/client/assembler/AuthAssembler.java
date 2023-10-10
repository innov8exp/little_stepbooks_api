package net.stepbooks.interfaces.client.assembler;

import net.stepbooks.domain.user.entity.UserEntity;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.model.JwtUserDetails;

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
