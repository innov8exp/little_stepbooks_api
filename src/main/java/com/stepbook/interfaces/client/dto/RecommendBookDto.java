package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.enums.RecommendType;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendBookDto extends BaseDto {

    private String bookName;
    private String author;
    private String coverImg;
    private String introduction;
    private Boolean isSerialized;
    private Boolean hasEnding;
    private RecommendType recommendType;
    private Integer sortIndex;
}
