package co.botechservices.novlnovl.domain.product.dto;

import co.botechservices.novlnovl.infrastructure.enums.ClientPlatform;
import co.botechservices.novlnovl.infrastructure.model.BaseDto;
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
