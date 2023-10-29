package net.stepbooks.domain.sms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import net.stepbooks.infrastructure.enums.SendStatus;
import net.stepbooks.infrastructure.enums.SmsType;
import net.stepbooks.infrastructure.model.BaseEntity;

@TableName("STEP_SMS_HISTORY")
public class SmsEntity extends BaseEntity {
    private String phoneNumber;
    private String smsContent;
    private Long validSeconds;
    private SmsType smsType;
    private SendStatus sendStatus;
}
