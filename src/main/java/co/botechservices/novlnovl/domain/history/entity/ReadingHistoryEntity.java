package co.botechservices.novlnovl.domain.history.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_READING_HISTORY")
public class ReadingHistoryEntity extends BaseEntity {
    private String bookId;
    private String chapterId;
    private String userId;
    private Long paragraphNumber;
}
