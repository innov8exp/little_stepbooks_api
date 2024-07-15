package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.Material;
import net.stepbooks.infrastructure.enums.SalesPlatform;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_PRODUCT")
public class Product extends BaseEntity {

    private StoreType storeType;

    private Integer sortIndex;

    private String skuCode;
    private String skuName;
    private int salesPlatforms;
    private ProductNature productNature;
    private String description;
    private BigDecimal price;
    private String coverImgId;
    private String coverImgUrl;

    private String videoId;
    private String videoUrl;

    private int materials;
    private ProductStatus status;
    private String detailImgId;
    private String tags;

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
