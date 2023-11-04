package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_PRODUCT_MEDIA_REF")
public class ProductMedia {
    private String id;
    private String productId;
    private String mediaId;
}
