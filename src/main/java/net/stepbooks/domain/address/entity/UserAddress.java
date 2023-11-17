package net.stepbooks.domain.address.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_USER_ADDRESS")
public class UserAddress extends BaseEntity {

    private String userId;
    private String recipientName;
    private String recipientPhone;
    private String recipientLocation;
    private String recipientAddress;
}
