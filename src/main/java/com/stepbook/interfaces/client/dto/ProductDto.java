package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.enums.ClientPlatform;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDto extends BaseDto {

    private String productNo;
    private BigDecimal coinAmount;
    private BigDecimal price;
    private ClientPlatform platform;
    private String storeProductId;
}
