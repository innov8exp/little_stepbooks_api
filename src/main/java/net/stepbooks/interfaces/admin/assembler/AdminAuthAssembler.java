package net.stepbooks.interfaces.admin.assembler;

import net.stepbooks.domain.admin.entity.AdminUser;
import net.stepbooks.infrastructure.model.JwtUserDetails;

public class AdminAuthAssembler {

    public static JwtUserDetails adminUserEntityToJwtUserDetails(AdminUser adminUser) {
        return JwtUserDetails.builder()
                .username(adminUser.getUsername())
                .nickname(adminUser.getNickname())
                .email(adminUser.getEmail())
                .build();
    }

}
