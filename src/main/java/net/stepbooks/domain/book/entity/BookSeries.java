package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "STEP_BOOK_SERIES")
public class BookSeries extends BaseEntity {
    private String seriesName;
    private String classificationId;
    private String description;
    private String coverImgId;
    private String coverImgUrl;

    @Deprecated
    private String detailImgId;
    private String detailImgUrl;
}
