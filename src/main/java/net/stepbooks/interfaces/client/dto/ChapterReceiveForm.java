package net.stepbooks.interfaces.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterReceiveForm {

    @NotNull
    private String bookId;

    /**
     * '*' 表示领取本书全部章节，'6,7,8,9,10'表示领取ID为6~10的5个章节（有声书）
     */
    @NotNull
    private String chapters;
}
