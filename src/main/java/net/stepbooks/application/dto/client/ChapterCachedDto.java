package net.stepbooks.application.dto.client;

import lombok.Data;

@Data
public class ChapterCachedDto {
    private Integer sortIndex;
    private String chapterName;
    private String bookId;
    private String content;
}
