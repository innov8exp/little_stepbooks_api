package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_VIRTUAL_GOODS_VIDEO")
public class VirtualGoodsVideoEntity extends BaseEntity {

    private String goodsId;
    private String name;
    private String coverId;
    private String coverUrl;
    private String videoId;
    private String videoUrl;

}
