package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_PRODUCT_VIRTUAL_GOODS_REF")
public class ProductVirtualGoods {

    private String id;
    private String productId;
    private String goodsId;

}
