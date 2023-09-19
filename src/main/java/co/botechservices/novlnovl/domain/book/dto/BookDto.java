package co.botechservices.novlnovl.domain.book.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto extends BaseDto {
    private String bookName;
    private String author;
    private String coverImg;
    private String introduction;
    private String[] keywords;
    private Boolean isSerialized;
    private Boolean hasEnding;
    private String contentLocation;
    private String status;
    private Long totalParagraphNumber;
}
