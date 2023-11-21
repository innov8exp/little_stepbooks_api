package net.stepbooks.interfaces.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.infrastructure.enums.Material;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookAndMaterialsDto {
    private String bookId;
    private String bookName;
    private String author;
    private String coverImgId;
    private String coverImgUrl;
    private String description;
    private String[] classifications;
    private Long chapterCount;
    private Long courseCount;
    private Material[] materials;
}