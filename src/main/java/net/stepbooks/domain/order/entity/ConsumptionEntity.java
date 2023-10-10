package net.stepbooks.domain.order.entity;

import net.stepbooks.infrastructure.enums.ClientPlatform;
import net.stepbooks.infrastructure.enums.ConsumeType;
import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_CONSUMPTION")
public class ConsumptionEntity extends BaseEntity {

    private String userId;
    private String bookId;
    private String chapterId;
    private String consumeOrderNo;
    private ClientPlatform clientPlatform;
    private BigDecimal coinAmount;
    private ConsumeType consumeType;
}
