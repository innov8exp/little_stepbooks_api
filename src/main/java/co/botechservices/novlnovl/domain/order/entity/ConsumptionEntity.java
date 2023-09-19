package co.botechservices.novlnovl.domain.order.entity;

import co.botechservices.novlnovl.infrastructure.enums.ClientPlatform;
import co.botechservices.novlnovl.infrastructure.enums.ConsumeType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("NOVL_CONSUMPTION")
public class ConsumptionEntity extends BaseEntity {

    private String userId;
    private String bookId;
    private String chapterId;
    private String consumeOrderNo;
    private ClientPlatform clientPlatform;
    private BigDecimal coinAmount;
    private ConsumeType consumeType;
}
