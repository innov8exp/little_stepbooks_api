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
    private String seriesId;
    private Integer seriesNo;

    /**
     * 第一级，第二级...
     */
    private String seriesName;

    private List<BookMedia> medias;

    private List<CourseDto> courses;
    private List<BookChapterDto> chapters;

    //暂时没有单本书的领取，将来有的话，要加上领取时间给前端排序用
    //private LocalDateTime receiveAt;
}
