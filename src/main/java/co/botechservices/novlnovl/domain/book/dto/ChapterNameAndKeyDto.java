package co.botechservices.novlnovl.domain.book.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterNameAndKeyDto {
    private String chapterName;
    private String storeKey;
}
