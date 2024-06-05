package net.stepbooks.interfaces.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyAudioAdminDto extends BaseDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    private String categoryId;
    private String goodsId;
    private String audioId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDay;
}
