package net.stepbooks.domain.media.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_DETAIL_IMAGE_CUT")
public class DetailImageCut extends BaseEntity {

    private Integer sortIndex;
    private String detailImgId;
    private String imgId;
    private String imgUrl;

}
