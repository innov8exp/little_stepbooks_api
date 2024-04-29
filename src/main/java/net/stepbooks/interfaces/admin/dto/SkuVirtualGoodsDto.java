package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.product.enums.RedeemCondition;

@Data
public class SkuVirtualGoodsDto {

    private String id;
    private String spuId;
    private String skuId;
    private String categoryId;
    private String goodsId;
    private RedeemCondition redeemCondition;

    private String categoryName; //额外内容
    private String goodsName; //额外内容
    private String goodsDescription; //额外内容
}
