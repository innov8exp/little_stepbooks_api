package net.stepbooks.domain.classification.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_CLASSIFICATION")
public class ClassificationEntity extends BaseEntity {

    private String classificationName;
    private Integer maxAge;
    private Integer minAge;
    private String description;
}
