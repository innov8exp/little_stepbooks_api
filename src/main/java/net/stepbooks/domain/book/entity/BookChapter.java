package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_BOOK_CHAPTER")
public class BookChapter extends BaseEntity {

    private String bookId;
    private Integer chapterNo;
    private String chapterName;
    private String description;
    private String imgId;
    private String imgUrl;
    private String audioId;
    private String audioUrl;
}
