package com.stepbook.domain.dict.dto;

import com.stepbook.infrastructure.model.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto extends BaseDto {
    private String categoryName;
    private String description;
    private Integer sortIndex;
}
