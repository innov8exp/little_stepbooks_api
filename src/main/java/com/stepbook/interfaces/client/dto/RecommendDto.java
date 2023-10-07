package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.enums.RecommendType;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendDto extends BaseDto {
    private String bookId;
    private String bookName;
    private Integer sortIndex;
    private String introduction;
    private RecommendType recommendType;
}
