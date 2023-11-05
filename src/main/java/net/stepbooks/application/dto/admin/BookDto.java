package net.stepbooks.application.dto.admin;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto extends BaseDto {
    private String bookName;
    private String author;
    private String coverImgId;
    private String coverImgUrl;
    private String description;
    private String[] classifications;
    private Long chapterCount;
    private Long courseCount;
}
