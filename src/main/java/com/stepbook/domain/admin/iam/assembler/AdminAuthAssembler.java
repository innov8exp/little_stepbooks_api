package com.stepbook.domain.admin.iam.assembler;

import com.stepbook.domain.admin.iam.entity.AdminUserEntity;
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
