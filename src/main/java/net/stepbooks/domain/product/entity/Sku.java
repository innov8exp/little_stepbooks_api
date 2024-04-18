package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_SKU")
public class Sku extends BaseEntity {
    private String spuId;
    private String skuCode;
    private String skuName;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private ProductStatus status;
}
