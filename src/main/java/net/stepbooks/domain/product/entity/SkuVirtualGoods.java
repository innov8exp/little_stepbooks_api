package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.stepbooks.domain.product.enums.RedeemCondition;

@Data
@TableName("STEP_SKU_VIRTUAL_GOODS_REF")
public class SkuVirtualGoods {

    private String id;

    @NotNull
    private String spuId;

    @NotNull
    private String skuId;
    private String categoryId;
    private String goodsId;
    private RedeemCondition redeemCondition;

}

