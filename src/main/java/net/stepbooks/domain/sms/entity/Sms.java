package net.stepbooks.domain.sms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.SendStatus;
import net.stepbooks.infrastructure.enums.SmsType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("STEP_SMS_HISTORY")
public class Sms extends BaseEntity {
    private String phone;
    private String content;
    private Long msgId;
    private SmsType smsType;
    private SendStatus status;
}
