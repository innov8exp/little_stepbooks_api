package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_VIRTUAL_GOODS")
public class VirtualGoodsEntity extends BaseEntity {

    private Integer sortIndex;
    private String name;
    private String description;
    private String categoryId;
    private String detailImgId;
    private int toAddMonth;

}
