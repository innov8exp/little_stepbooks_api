package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

@Data
public class SkuPhysicalGoodsDto {
    private String id;
    private String spuId;
    private String skuId;
    private String goodsId;

    private String goodsName; //额外内容
    private String goodsDescription; //额外内容
}
