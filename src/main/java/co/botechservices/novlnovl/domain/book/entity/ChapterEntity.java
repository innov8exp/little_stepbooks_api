package co.botechservices.novlnovl.domain.book.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_CHAPTER")
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
