package co.botechservices.novlnovl.domain.user.assembler;

import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.infrastructure.assembler.BaseAssembler;
import co.botechservices.novlnovl.infrastructure.model.JwtUserDetails;

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
