package com.stepbook.domain.order.dto;

import com.stepbook.infrastructure.enums.ClientPlatform;
import com.stepbook.infrastructure.enums.ConsumeType;
import com.stepbook.infrastructure.model.BaseDto;
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
