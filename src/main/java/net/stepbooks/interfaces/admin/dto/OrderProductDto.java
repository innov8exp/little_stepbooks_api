package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.Material;
import net.stepbooks.infrastructure.enums.SalesPlatform;

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

    public SalesPlatform[] getParsedSalesPlatforms() {
        return SalesPlatform.parseSalesPlatforms(salesPlatforms);
    }

    public Material[] getParsedMaterials() {
        return Material.parseMaterials(materials);
    }

    public void setParsedSalesPlatforms(SalesPlatform[] salesPlatforms) {
        this.salesPlatforms = SalesPlatform.parseSalesPlatforms(salesPlatforms);
    }

    public void setParsedMaterials(Material[] materials) {
        this.materials = Material.parseMaterials(materials);
    }
}
