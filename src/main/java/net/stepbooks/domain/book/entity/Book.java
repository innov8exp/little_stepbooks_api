package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "STEP_BOOK")
public class Book extends BaseEntity {
    private String bookName;
    private String author;
    private String bookImgId;
    private String bookImgUrl;
    private String description;
    private Integer totalPageNumber;
    private String duration;
    private String seriesId;
}
