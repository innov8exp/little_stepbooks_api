package co.botechservices.novlnovl.domain.email.entity;

import co.botechservices.novlnovl.infrastructure.enums.EmailStatus;
import co.botechservices.novlnovl.infrastructure.enums.EmailType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_EMAIL_HISTORY")
public class EmailHistoryEntity extends BaseEntity {

    private String email;
    private String verificationCode;
    private Long validSeconds;
    private EmailType emailType;
    private EmailStatus status;
}
