package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("STEP_SKU_PHYSICAL_GOODS_REF")
public class SkuPhysicalGoods {

    private String id;

    @NotNull
    private String spuId;

    @NotNull
    private String skuId;
    private String goodsId;

}
