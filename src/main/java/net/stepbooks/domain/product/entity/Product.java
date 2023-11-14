package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.product.enums.ProductNature;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_PRODUCT")
public class Product extends BaseEntity {

    private String skuCode;
    private String skuName;
    private String salesPlatform;
    private ProductNature productNature;
    private String description;
    private BigDecimal price;
    private String coverImgId;
    private String coverImgUrl;
    private String resources;
    private String bookSetId;
    private ProductStatus status;
}
