package net.stepbooks.application.dto.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterNameAndKeyDto {
    private String chapterName;
    private String storeKey;
}
