package net.stepbooks.domain.media.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.MediaType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_MEDIA")
public class MediaEntity extends BaseEntity {

    private String objectName;
    private MediaType objectType;
    private String objectUrl;
    private String s3ObjectId;
    private String s3Bucket;
    private String storePath;
}
