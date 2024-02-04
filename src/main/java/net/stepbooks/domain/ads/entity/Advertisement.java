package net.stepbooks.domain.ads.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_ADVERTISEMENT")
public class Advertisement extends BaseEntity {
    private String productId;
    private String introduction;
    private String adsImgId;
    private String adsImgUrl;
    private Integer sortIndex;
    private AdsType adsType;
    private String actionUrl;
}
