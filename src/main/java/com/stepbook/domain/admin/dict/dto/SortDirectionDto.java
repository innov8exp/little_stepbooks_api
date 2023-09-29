package com.stepbook.domain.admin.dict.dto;

import com.stepbook.infrastructure.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDirectionDto {
    private SortDirection direction;
}
