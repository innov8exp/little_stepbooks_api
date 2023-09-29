package com.stepbook.domain.email.entity;

import com.stepbook.infrastructure.enums.EmailStatus;
import com.stepbook.infrastructure.enums.EmailType;
import com.stepbook.infrastructure.model.BaseEntity;
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
