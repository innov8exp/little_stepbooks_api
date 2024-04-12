package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_PRODUCT_PHYSICAL_GOODS_REF")
public class ProductPhysicalGoods {

    private String id;
    private String productId;
    private String goodsId;

}
