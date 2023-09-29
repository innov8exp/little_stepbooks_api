package com.stepbook.domain.history.entity;

import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_READING_HISTORY")
public class ReadingHistoryEntity extends BaseEntity {
    private String bookId;
    private String chapterId;
    private String userId;
    private Long paragraphNumber;
}
