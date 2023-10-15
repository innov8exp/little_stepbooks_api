package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_BOOK_CONTENT")
public class BookContentEntity extends BaseEntity {

    private String bookId;
    private String pageDescription;
    private String pageNumber;
    private String pageImgLink;
    private String pageAudioLink;
}
