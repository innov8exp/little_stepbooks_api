package com.stepbook.domain.book.dto;

import com.stepbook.infrastructure.model.BaseDto;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChapterWithHistoryDto extends BaseDto {
    private Integer chapterNumber;
    private String chapterName;
    private String introduction;
    private String bookId;
    private String contentLink;
    private Long totalParagraphNumber;
    private Long paragraphNumber;
    private Boolean needPay;
    private Boolean isLastReadChapter;
    private BigDecimal price;
    private Boolean unlocked;
}
