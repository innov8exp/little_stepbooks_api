package com.stepbook.domain.dict.dto;

import com.stepbook.infrastructure.model.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto extends BaseDto {
    private String tagName;
    private String description;
}
