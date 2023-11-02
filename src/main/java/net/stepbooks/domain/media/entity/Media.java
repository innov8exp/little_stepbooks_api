package net.stepbooks.domain.media.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.stepbooks.infrastructure.enums.MediaType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("STEP_MEDIA")
public class Media extends BaseEntity {

    private String fileName;
    private Long fileSize;
    private Boolean publicAccess;
    private String objectName;
    private MediaType objectType;
    private String s3ObjectId;
    private String s3Bucket;
    private String storePath;
}
