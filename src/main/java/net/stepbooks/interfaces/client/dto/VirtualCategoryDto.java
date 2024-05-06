package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseDto;

import java.util.List;

@Data
public class VirtualCategoryDto extends BaseDto {
    private String parentId;
    private Integer sortIndex;
    private String name;
    private String coverId;
    private String coverUrl;
    private String detailImgId;
    private PublishStatus status;
    private VirtualCategoryType type;
    private List<VirtualCategoryDto> children;
}
