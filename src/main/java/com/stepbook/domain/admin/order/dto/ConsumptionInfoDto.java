package com.stepbook.domain.admin.order.dto;

import com.stepbook.infrastructure.enums.ClientPlatform;
import com.stepbook.infrastructure.enums.ConsumeType;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConsumptionInfoDto extends BaseDto {

    private String userId;
    private String username;
    private String nickname;
    private String bookId;
    private String bookName;
    private String chapterId;
    private String chapterNumber;
    private String chapterName;
    private String consumeOrderNo;
    private ClientPlatform clientPlatform;
    private BigDecimal coinAmount;
    private ConsumeType consumeType;
}
