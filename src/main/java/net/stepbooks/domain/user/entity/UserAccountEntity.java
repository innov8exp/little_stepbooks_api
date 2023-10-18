package net.stepbooks.domain.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_USER_ACCOUNT")
public class UserAccountEntity extends BaseEntity {
    private String userId;
    private BigDecimal coinBalance;
}
