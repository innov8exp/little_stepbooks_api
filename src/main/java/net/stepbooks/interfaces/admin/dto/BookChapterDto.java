package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookChapterDto extends BaseDto {

    private String bookId;
    private Integer chapterNo;
    private String chapterName;
    private String description;
    private String imgId;
    private String imgUrl;
    private String imgKey;
    private String audioId;
    private String audioUrl;
    private String audioKey;
}
