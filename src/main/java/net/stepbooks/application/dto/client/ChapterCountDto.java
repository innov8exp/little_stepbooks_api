package net.stepbooks.application.dto.client;

import lombok.Data;

@Data
public class ChapterCountDto {
    private String bookId;
    private Integer chapterCount;
}
