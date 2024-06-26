package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_VIRTUAL_GOODS_COURSE")
public class VirtualGoodsCourseEntity extends BaseEntity {

    private Integer sortIndex;
    private String categoryId;
    private String goodsId;
    private String courseId;

}
