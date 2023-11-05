package net.stepbooks.interfaces.client.assembler;

import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.model.JwtUserDetails;

public class AuthAssembler extends BaseAssembler {

    public static JwtUserDetails userEntityToJwtUserDetails(User user) {
        return JwtUserDetails.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .googleId(user.getGoogleId())
                .facebookId(user.getFacebookId())
                .build();
    }

    public static JwtUserDetails guestEntityToJwtUserDetails(String deviceId) {
        return JwtUserDetails.builder()
                .username(deviceId)
                .build();
    }

}
