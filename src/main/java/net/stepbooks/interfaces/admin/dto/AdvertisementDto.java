package net.stepbooks.interfaces.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.AdsType;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisementDto extends BaseDto {
    private String productId;
    private String skuCode;
    private String skuName;
    private String introduction;
    private String adsImgId;
    private String adsImgUrl;
    private Integer sortIndex;
    private AdsType adsType;
}
