package net.stepbooks.application.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.ClientPlatform;
import net.stepbooks.infrastructure.enums.ConsumeType;
import net.stepbooks.infrastructure.model.BaseDto;

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
