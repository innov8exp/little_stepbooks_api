package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.goods.enums.VirtualCategoryType;
import net.stepbooks.infrastructure.enums.PublishStatus;
import net.stepbooks.infrastructure.model.BaseDto;
import net.stepbooks.interfaces.client.dto.VirtualCategoryProductDto;

@EqualsAndHashCode(callSuper = true)
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

    /**
     * 虚拟大类和商品的对应关系
     */
    private VirtualCategoryProductDto virtualCategoryProduct;

    private VirtualCategoryAdminDto parent;

}
