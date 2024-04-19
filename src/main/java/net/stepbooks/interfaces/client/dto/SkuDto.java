package net.stepbooks.interfaces.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.stepbooks.domain.product.entity.Product;

@Data
public class SkuDto {

    @NotNull
    private String id;
    private String spuId;
    private String skuCode;

    private Product product;
    private int quantity;
}
