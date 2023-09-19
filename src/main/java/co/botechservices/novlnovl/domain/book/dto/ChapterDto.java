package co.botechservices.novlnovl.domain.book.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChapterDto extends BaseDto {
    private Integer chapterNumber;
    private String chapterName;
    private String introduction;
    private String bookId;
    private String contentLink;
    private String content;
    private Long totalParagraphNumber;
    private Boolean needPay;
    private BigDecimal price;
    private Boolean unlocked;
    private String storeKey;
}
