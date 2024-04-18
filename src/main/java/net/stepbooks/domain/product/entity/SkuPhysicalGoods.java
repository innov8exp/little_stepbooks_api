package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_SKU_PHYSICAL_GOODS_REF")
public class SkuPhysicalGoods {

    private String id;
    private String spuId;
    private String skuId;
    private String goodsId;

}
