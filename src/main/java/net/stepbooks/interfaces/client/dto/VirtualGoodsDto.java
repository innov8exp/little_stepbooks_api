package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.goods.entity.VirtualGoodsAudioEntity;
import net.stepbooks.domain.goods.entity.VirtualGoodsVideoEntity;
import net.stepbooks.infrastructure.model.BaseDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualGoodsDto extends BaseDto {

    private Integer sortIndex;
    private String name;
    private String description;
    private String categoryId;
    private int toAddMonth;

    private List<VirtualGoodsAudioEntity> audioList;
    private List<VirtualGoodsVideoEntity> videoList;

}
