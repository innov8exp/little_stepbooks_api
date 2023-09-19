package co.botechservices.novlnovl.domain.history.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_FINISH_HISTORY")
public class FinishHistoryEntity extends BaseEntity {

    private String userId;
    private String bookId;
}
