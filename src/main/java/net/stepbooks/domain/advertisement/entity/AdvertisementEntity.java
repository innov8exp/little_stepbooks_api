package net.stepbooks.domain.advertisement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_ADVERTISEMENT")
public class AdvertisementEntity extends BaseEntity {
    private String bookId;
    private String introduction;
    private String adsImg;
    private Integer sortIndex;
    private AdsType adsType;
}
