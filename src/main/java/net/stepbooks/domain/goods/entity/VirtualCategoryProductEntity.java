package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.goods.enums.VirtualCategoryProductDisplayTime;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_VIRTUAL_CATEGORY_PRODUCT")
public class VirtualCategoryProductEntity extends BaseEntity {

    private String categoryId;
    private String productId;

    private VirtualCategoryProductDisplayTime displayTime;

}
