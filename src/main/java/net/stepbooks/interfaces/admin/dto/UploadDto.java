package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.infrastructure.enums.AssetDomain;
import net.stepbooks.infrastructure.enums.AccessPermission;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDto {
    private AccessPermission permission;
    private AssetDomain domain;
}
