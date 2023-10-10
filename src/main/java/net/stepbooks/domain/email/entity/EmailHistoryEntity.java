package net.stepbooks.domain.email.entity;

import net.stepbooks.infrastructure.enums.EmailStatus;
import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_EMAIL_HISTORY")
public class EmailHistoryEntity extends BaseEntity {

    private String email;
    private String verificationCode;
    private Long validSeconds;
    private EmailType emailType;
    private EmailStatus status;
}
