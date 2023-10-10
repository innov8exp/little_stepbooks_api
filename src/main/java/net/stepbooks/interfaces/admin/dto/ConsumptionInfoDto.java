package net.stepbooks.interfaces.admin.dto;

import net.stepbooks.infrastructure.enums.ClientPlatform;
import net.stepbooks.infrastructure.enums.ConsumeType;
import net.stepbooks.infrastructure.model.BaseDto;
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
