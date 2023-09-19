package co.botechservices.novlnovl.domain.book.dto;

import lombok.Data;

@Data
public class ChapterCountDto {
    private String bookId;
    private Integer chapterCount;
}
