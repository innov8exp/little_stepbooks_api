package net.stepbooks.domain.classification.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_CLASSIFICATION")
public class Classification extends BaseEntity {

    private String classificationName;
    private Float maxAge;
    private Float minAge;
    private String description;
}
