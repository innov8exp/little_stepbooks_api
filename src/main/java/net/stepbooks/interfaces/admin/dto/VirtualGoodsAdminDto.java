package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class VirtualGoodsAdminDto extends BaseDto {

    private Integer sortIndex;
    private String name;
    private String description;
    private String categoryId;
    private int toAddMonth;

    private VirtualCategoryAdminDto category;

}
