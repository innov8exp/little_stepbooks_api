package co.botechservices.novlnovl.domain.order.dto;

import co.botechservices.novlnovl.infrastructure.enums.ClientPlatform;
import co.botechservices.novlnovl.infrastructure.enums.ConsumeType;
import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConsumptionDto extends BaseDto {

    private String userId;
    private String bookId;
    private String chapterId;
    private String consumeOrderNo;
    private ClientPlatform clientPlatform;
    private BigDecimal coinAmount;
    private ConsumeType consumeType;
}
