package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PHYSICAL_GOODS")
public class PhysicalGoodsEntity extends BaseEntity {

    private String name;
    private String description;
    private String imgId;
    private String imgUrl;

}
