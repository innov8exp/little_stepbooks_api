package co.botechservices.novlnovl.domain.product.entity;

import co.botechservices.novlnovl.infrastructure.enums.ClientPlatform;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_PRODUCT")
public class ProductEntity extends BaseEntity {
    private String productNo;
    private BigDecimal coinAmount;
    private BigDecimal price;
    private ClientPlatform platform;
    private String storeProductId;
}
