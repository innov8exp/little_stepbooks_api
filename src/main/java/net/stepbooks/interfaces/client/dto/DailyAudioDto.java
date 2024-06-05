package net.stepbooks.interfaces.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyAudioDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    private String parentCategoryId;

    private String categoryId;
    private String goodsId;
    private String audioId;

    private List<VirtualGoodsDto> goods;

}
