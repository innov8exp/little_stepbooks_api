package net.stepbooks.interfaces.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.stepbooks.domain.goods.entity.PhysicalGoodsEntity;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuDto {

    @NotNull
    private String id;

    @NotNull
    private String spuId;
    private String skuName;

    private BigDecimal originalPrice;
    private BigDecimal price;

    private List<PhysicalGoodsEntity> physicalGoods;

    private int quantity;
}
