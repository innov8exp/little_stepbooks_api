package net.stepbooks.domain.bookshelf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_BOOKSHELF_ADD_LOG")
public class BookshelfAddLog extends BaseEntity {

    private String userId;
    private String bookId;
}
