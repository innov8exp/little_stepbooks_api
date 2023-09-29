package com.stepbook.domain.ads.dto;

import com.stepbook.infrastructure.enums.AdsType;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdvertisementDto extends BaseDto {
    private String bookId;
    private String bookName;
    private String bookIntroduction;
    private String bookImg;
    private String bookAuthor;
    private String introduction;
    private String adsImg;
    private Integer sortIndex;
    private AdsType adsType;
}
