package net.stepbooks.interfaces.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.stepbooks.domain.product.entity.Product;

@Data
public class SkuDto {

    @NotNull
    private String id;

    @NotNull
    private String spuId;
    private String skuCode;

    @Deprecated
    private Product product;
    private int quantity;
}
