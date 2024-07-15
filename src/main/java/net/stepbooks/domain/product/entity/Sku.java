package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.stepbooks.domain.product.enums.ProductStatus;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_SKU")
public class Sku extends BaseEntity {

    private StoreType storeType;

    private Integer sortIndex;

    @NotNull
    private String spuId;
    private String skuName;
    private BigDecimal originalPrice;
    private BigDecimal price;
    private ProductStatus status;
}
