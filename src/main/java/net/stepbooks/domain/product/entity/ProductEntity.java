package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.enums.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_PRODUCT")
public class ProductEntity extends BaseEntity {

    private String skuNo;
    private String productName;
    private PackageType packageType;
    private ProductType productType;
    private ChargeType chargeType;
    private SalesPlatform salesPlatform;
    private String productDescription;
    private Double price;
    private String productImgLink;
    private ProductObjectType productObjectType;
}
