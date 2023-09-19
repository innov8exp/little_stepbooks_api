package co.botechservices.novlnovl.domain.feedback.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_FEEDBACK")
public class FeedbackEntity extends BaseEntity {
    private String userId;
    private String content;
}
