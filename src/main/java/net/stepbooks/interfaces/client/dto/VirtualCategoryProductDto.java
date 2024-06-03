package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.domain.goods.enums.VirtualCategoryProductDisplayTime;

@Data
public class VirtualCategoryProductDto {

    private String categoryId;
    private String productId;
    private VirtualCategoryProductDisplayTime displayTime;

}
