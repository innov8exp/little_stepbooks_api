package com.stepbook.domain.promotion.entity;

import com.stepbook.infrastructure.enums.PromotionType;
import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
