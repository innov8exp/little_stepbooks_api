package com.stepbook.interfaces.client.dto;

import lombok.Data;

@Data
public class ChapterCachedDto {
    private Integer sortIndex;
    private String chapterName;
    private String bookId;
    private String content;
}
