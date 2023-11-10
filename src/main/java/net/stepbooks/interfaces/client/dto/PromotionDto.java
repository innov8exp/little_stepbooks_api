package net.stepbooks.interfaces.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.PromotionType;
import net.stepbooks.infrastructure.model.BaseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class PromotionDto extends BaseDto {
    private String bookId;
    private String bookName;
    private PromotionType promotionType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime limitTo;
    private BigDecimal discountPercent;
    private BigDecimal coinAmount;
    private BigDecimal originalCoinAmount;
}
