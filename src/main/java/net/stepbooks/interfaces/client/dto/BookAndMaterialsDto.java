package net.stepbooks.interfaces.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.domain.book.entity.BookMedia;
import net.stepbooks.infrastructure.enums.Material;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookAndMaterialsDto {
    private String bookId;
    private String bookName;
    private String author;
    private String bookImgId;
    private String bookImgUrl;
    private String description;
    private String[] classifications;
    private long chapterCount;
    private long courseCount;
    private List<BookMedia> medias;
    private Material[] materials;
}
