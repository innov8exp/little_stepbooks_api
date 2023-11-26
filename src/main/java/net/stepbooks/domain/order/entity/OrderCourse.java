package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_ORDER_COURSE_REF")
public class OrderCourse {
    private String id;
    private String productId;
    private String userId;
    private String orderId;
    private String bookId;
    private String courseId;
}
