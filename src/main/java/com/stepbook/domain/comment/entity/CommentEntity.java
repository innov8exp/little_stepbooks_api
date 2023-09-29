package com.stepbook.domain.comment.entity;

import com.stepbook.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_COMMENT")
public class CommentEntity extends BaseEntity {
    private String bookId;
    private String userId;
    private String content;
}
