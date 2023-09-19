package co.botechservices.novlnovl.domain.ads.entity;

import co.botechservices.novlnovl.infrastructure.enums.RecommendType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_RECOMMEND")
public class RecommendEntity extends BaseEntity {
    private String bookId;
    private Integer sortIndex;
    private String introduction;
    private RecommendType recommendType;
}
