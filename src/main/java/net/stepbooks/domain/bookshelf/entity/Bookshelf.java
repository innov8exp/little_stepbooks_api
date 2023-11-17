package net.stepbooks.domain.bookshelf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_BOOKSHELF")
public class Bookshelf extends BaseEntity {
    private String bookId;
    private String userId;
    private String bookSetId;
    private String bookSetCode;
    private Integer sortIndex;
}
