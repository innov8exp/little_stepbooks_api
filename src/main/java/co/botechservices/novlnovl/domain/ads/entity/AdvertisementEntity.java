package co.botechservices.novlnovl.domain.ads.entity;

import co.botechservices.novlnovl.infrastructure.enums.AdsType;
import co.botechservices.novlnovl.infrastructure.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("NOVL_ADVERTISEMENT")
public class AdvertisementEntity extends BaseEntity {
    private String bookId;
    private String introduction;
    private String adsImg;
    private Integer sortIndex;
    private AdsType adsType;
}
