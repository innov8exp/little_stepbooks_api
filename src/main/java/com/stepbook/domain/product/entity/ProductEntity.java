package com.stepbook.domain.product.entity;

import com.stepbook.infrastructure.enums.ClientPlatform;
import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PRODUCT")
public class ProductEntity extends BaseEntity {
    private String productNo;
    private BigDecimal coinAmount;
    private BigDecimal price;
    private ClientPlatform platform;
    private String storeProductId;
}
