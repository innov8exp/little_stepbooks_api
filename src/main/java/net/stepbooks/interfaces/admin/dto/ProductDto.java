package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import net.stepbooks.domain.media.entity.Media;
import net.stepbooks.domain.product.entity.ProductMedia;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.Material;
import net.stepbooks.infrastructure.enums.SalesPlatform;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private String skuCode;
    private String skuName;
    private int salesPlatforms;
    private int materials;
    private ProductNature productNature;
    private String description;
    private BigDecimal price;
    private String coverImgId;
    private String coverImgUrl;
    private String bookSetId;
    private String bookSetCode;
    private String bookSetName;
    private List<ProductMediaDto> medias;
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
