package net.stepbooks.domain.exercise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_EXERCISE")
public class Exercise extends BaseEntity {

    private String courseId;
    private String name;
    private String nature;
    private Integer sortNo;
    private String coverImgId;
    private String coverImgUrl;
    private String json;
    private String answerJson;

}
