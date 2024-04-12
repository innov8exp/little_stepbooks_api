package net.stepbooks.domain.pairedread.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PAIRED_READ_COLLECTION")
public class PairedReadCollection extends BaseEntity {

    private String coverImgId;
    private String coverImgUrl;

    private String detailImgId;
    private String detailImgUrl;

    private String name;
    private String description;

    private PublishStatus status;

}
