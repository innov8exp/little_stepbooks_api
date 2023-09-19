package co.botechservices.novlnovl.domain.user.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_USER_ACCOUNT")
public class UserAccountEntity extends BaseEntity {
    private String userId;
    private BigDecimal coinBalance;
}
