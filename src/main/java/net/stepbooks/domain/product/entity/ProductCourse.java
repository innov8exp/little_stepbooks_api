package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_PRODUCT_COURSE_REF")
public class ProductCourse {
    private String id;
    private String productId;
    private String courseId;
}
