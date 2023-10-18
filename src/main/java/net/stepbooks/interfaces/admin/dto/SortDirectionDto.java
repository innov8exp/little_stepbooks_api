package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.infrastructure.enums.SortDirection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDirectionDto {
    private SortDirection direction;
}
