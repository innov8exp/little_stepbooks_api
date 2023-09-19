package co.botechservices.novlnovl.domain.dict.entity;

import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_TAG")
public class TagEntity extends BaseEntity {
    private String tagName;
    private String description;
}
