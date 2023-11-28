package net.stepbooks.domain.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_READING_HISTORY")
public class ReadingHistory extends BaseEntity {
    private String bookId;
    private String userId;
    private Integer maxChapterNo;
}
