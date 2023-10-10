package net.stepbooks.domain.price.entity;

import net.stepbooks.infrastructure.enums.ChargeType;
import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_PRICE")
public class PriceEntity extends BaseEntity {
    private String bookId;
    private ChargeType chargeType;
    private BigDecimal price;
}
