package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class ChapterCountDto {
    private String bookId;
    private Integer chapterCount;
}
