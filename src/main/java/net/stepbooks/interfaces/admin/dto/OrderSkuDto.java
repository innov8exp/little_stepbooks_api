package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSkuDto {

    private String orderId;

    private String spuId;
    /**
     * 这里的spuName=product.skuName，
     * 由于历史原因，Product的名称使用的是skuName，修改比较麻烦，不去改他了
     */
    private String spuName;
    private String spuDescription;
    private String spuCoverImgId;
    private String spuCoverImgUrl;

    private String skuId;
    private String skuName;
    private BigDecimal skuPrice;

    private int quantity;

}
