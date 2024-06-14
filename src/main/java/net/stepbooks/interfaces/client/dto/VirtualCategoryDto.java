package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualCategoryDto extends BaseDto {
    private Boolean free;
    private String parentId;
    private Integer sortIndex;
    private String name;
    private String description;
    private String coverId;
    private String coverUrl;
    private String detailImgId;
    private PublishStatus status;
    private VirtualCategoryType type;
    private String tags;
    private List<VirtualCategoryDto> children;
}
