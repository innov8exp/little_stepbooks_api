package net.stepbooks.domain.advertisement.entity;

import net.stepbooks.infrastructure.enums.RecommendType;
import net.stepbooks.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_RECOMMEND")
public class RecommendEntity extends BaseEntity {
    private String bookId;
    private Integer sortIndex;
    private String introduction;
    private RecommendType recommendType;
}
