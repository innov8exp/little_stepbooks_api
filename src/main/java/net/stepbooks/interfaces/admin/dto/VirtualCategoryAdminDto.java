package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseDto;

@Data
public class VirtualCategoryAdminDto extends BaseDto {

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

    /**
     * 相关的销售产品
     */
    private ProductDto relativeProduct;

    private VirtualCategoryAdminDto parent;

}
