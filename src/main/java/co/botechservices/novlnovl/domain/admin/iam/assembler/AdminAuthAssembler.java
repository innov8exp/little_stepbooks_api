package co.botechservices.novlnovl.domain.admin.iam.assembler;

import co.botechservices.novlnovl.domain.admin.iam.entity.AdminUserEntity;
import co.botechservices.novlnovl.infrastructure.model.JwtUserDetails;

public class AdminAuthAssembler {

    public static JwtUserDetails adminUserEntityToJwtUserDetails(AdminUserEntity adminUserEntity) {
        return JwtUserDetails.builder()
                .username(adminUserEntity.getUsername())
                .nickname(adminUserEntity.getNickname())
                .email(adminUserEntity.getEmail())
                .build();
    }

}
