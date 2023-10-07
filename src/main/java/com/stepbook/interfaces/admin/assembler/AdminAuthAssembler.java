package com.stepbook.interfaces.admin.assembler;

import com.stepbook.domain.admin.entity.AdminUserEntity;
import com.stepbook.infrastructure.model.JwtUserDetails;

public class AdminAuthAssembler {

    public static JwtUserDetails adminUserEntityToJwtUserDetails(AdminUserEntity adminUserEntity) {
        return JwtUserDetails.builder()
                .username(adminUserEntity.getUsername())
                .nickname(adminUserEntity.getNickname())
                .email(adminUserEntity.getEmail())
                .build();
    }

}
