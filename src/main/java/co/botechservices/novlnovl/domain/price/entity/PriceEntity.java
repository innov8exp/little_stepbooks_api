package co.botechservices.novlnovl.domain.price.entity;

import co.botechservices.novlnovl.infrastructure.enums.ChargeType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_PRICE")
public class PriceEntity extends BaseEntity {
    private String bookId;
    private ChargeType chargeType;
    private BigDecimal price;
}
