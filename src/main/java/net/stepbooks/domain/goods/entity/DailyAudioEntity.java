package net.stepbooks.domain.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_DAILY_AUDIO")
public class DailyAudioEntity extends BaseEntity {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;

    private String categoryId;
    private String goodsId;
    private String audioId;

}
