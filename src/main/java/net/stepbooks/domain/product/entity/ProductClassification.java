package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_PRODUCT_CLASSIFICATION_REF")
public class ProductClassification {
    private String id;
    private String productId;
    private String classificationId;
}
