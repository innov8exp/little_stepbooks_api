package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private String orderId;
    private String productId;
    private String skuCode;
    private String skuName;
    private BigDecimal price;
    private ProductNature productNature;
    private String[] resources;
    private int quantity;
    private int salesPlatforms;
    private int materials;
    private String description;
    private String coverImgId;
    private String coverImgUrl;
    private ProductStatus status;
}
