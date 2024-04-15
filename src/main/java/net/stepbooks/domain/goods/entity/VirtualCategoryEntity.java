package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_VIRTUAL_CATEGORY")
public class VirtualCategoryEntity extends BaseEntity {

    private Integer sortIndex;
    private String name;
    private String coverId;
    private String coverUrl;
    private PublishStatus status;
    private VirtualCategoryType type;

}