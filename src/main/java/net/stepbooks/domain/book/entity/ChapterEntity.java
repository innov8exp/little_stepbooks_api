package net.stepbooks.domain.book.entity;

import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_CHAPTER")
@Deprecated
public class ChapterEntity extends BaseEntity {
    private Integer chapterNumber;
    private String chapterName;
    private String introduction;
    private String bookId;
    private String contentLink;
    private Long totalParagraphNumber;
    private Boolean needPay;
    private String storeKey;
}
