package net.stepbooks.interfaces.admin.assembler;

import net.stepbooks.domain.admin.entity.AdminUserEntity;
import net.stepbooks.infrastructure.model.JwtUserDetails;

public class AdminAuthAssembler {

    public static JwtUserDetails adminUserEntityToJwtUserDetails(AdminUserEntity adminUserEntity) {
        return JwtUserDetails.builder()
                .username(adminUserEntity.getUsername())
                .nickname(adminUserEntity.getNickname())
                .email(adminUserEntity.getEmail())
                .build();
    }

}
