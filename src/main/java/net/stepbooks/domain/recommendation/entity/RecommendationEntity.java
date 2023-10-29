package net.stepbooks.domain.recommendation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.RecommendType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_RECOMMENDATION")
public class RecommendationEntity extends BaseEntity {
    private String bookId;
    private Integer sortIndex;
    private String introduction;
    private RecommendType recommendType;
}
