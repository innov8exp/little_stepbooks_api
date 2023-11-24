package net.stepbooks.domain.product.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_PRODUCT_BOOK_REF")
public class ProductBook {
    private String id;
    private String productId;
    private String bookId;
}
