package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.domain.book.entity.BookMedia;
import net.stepbooks.infrastructure.model.BaseDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto extends BaseDto {
    private String bookName;
    private String author;
    private String bookImgId;
    private String bookImgUrl;
    private String description;
    private String[] classifications;
    private Long chapterCount;
    private Long courseCount;
    private Long qrcodeCount;
    private String duration;
    private List<BookMedia> medias;
}
