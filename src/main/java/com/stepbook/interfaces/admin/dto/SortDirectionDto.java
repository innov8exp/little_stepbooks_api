package com.stepbook.interfaces.admin.dto;

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
