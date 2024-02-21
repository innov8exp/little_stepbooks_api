package net.stepbooks.interfaces.admin.dto;


import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSeriesDto extends BaseDto {

    private String seriesName;
    private String classificationId;
    private String description;
    private String coverImgId;
    private String coverImgUrl;
    private String detailImgId;
    private String detailImgUrl;

    private List<BookDto> books;
}
