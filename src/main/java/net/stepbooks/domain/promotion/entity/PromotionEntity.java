package net.stepbooks.domain.promotion.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.PromotionType;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PROMOTION")
public class PromotionEntity extends BaseEntity {

    private String bookId;
    private PromotionType promotionType;
    private LocalDateTime limitFrom;
    private LocalDateTime limitTo;
    private BigDecimal discountPercent;
    private BigDecimal coinAmount;
}
