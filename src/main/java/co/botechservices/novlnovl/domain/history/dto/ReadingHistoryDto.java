package co.botechservices.novlnovl.domain.history.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadingHistoryDto extends BaseDto {
    private String bookId;
    private String chapterId;
    private String userId;
    private Long paragraphNumber;
}
