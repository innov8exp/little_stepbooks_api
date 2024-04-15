package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_VIRTUAL_GOODS_AUDIO")
public class VirtualGoodsAudioEntity extends BaseEntity {

    private Integer sortIndex;
    private String categoryId;
    private String goodsId;
    private String name;
    private String coverId;
    private String coverUrl;
    private String audioId;
    private String audioUrl;
    private String duration;

}
