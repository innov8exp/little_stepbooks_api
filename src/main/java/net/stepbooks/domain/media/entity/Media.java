package net.stepbooks.domain.media.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.enums.AccessPermission;
import net.stepbooks.infrastructure.enums.AssetDomain;
import net.stepbooks.infrastructure.enums.MediaType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_MEDIA")
public class Media extends BaseEntity {
    private String fileName;
    private Long fileSize;
    private AccessPermission accessPermission;
    private AssetDomain assetDomain;
    private String objectName;
    private MediaType objectType;
    private String objectKey;
    private String bucketName;
    private String storePath;
    private String objectUrl;
}
