package net.stepbooks.application.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.ClientPlatform;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoinDto extends BaseDto {

    private String productNo;
    private BigDecimal coinAmount;
    private BigDecimal price;
    private ClientPlatform platform;
    private String storeProductId;
}
