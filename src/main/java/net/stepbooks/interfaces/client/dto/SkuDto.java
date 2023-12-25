package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.domain.product.entity.Product;

@Data
public class SkuDto {
    private String skuCode;
    private String productId;
    private Product product;
    private int quantity;
}
