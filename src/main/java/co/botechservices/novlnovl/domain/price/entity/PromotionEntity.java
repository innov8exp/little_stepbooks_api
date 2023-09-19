package co.botechservices.novlnovl.domain.price.entity;

import co.botechservices.novlnovl.infrastructure.enums.PromotionType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_PROMOTION")
public class PromotionEntity extends BaseEntity {

    private String bookId;
    private PromotionType promotionType;
    private LocalDateTime limitFrom;
    private LocalDateTime limitTo;
    private BigDecimal discountPercent;
    private BigDecimal coinAmount;
}
