package net.stepbooks.domain.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_ORDER_PHYSICAL_GOODS_REF")
public class OrderPhysicalGoods {

    private String id;
    private String orderId;
    private String userId;

    @NotNull
    private String spuId;

    @NotNull
    private String skuId;
    private String goodsId;

}
