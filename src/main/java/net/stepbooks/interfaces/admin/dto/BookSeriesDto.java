package net.stepbooks.interfaces.admin.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

import java.time.LocalDateTime;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 当前用户的领取时间，因为客户端需要用这个时间和伴读营领取记录进行新旧排序
     */
    private LocalDateTime receiveAt;
}
