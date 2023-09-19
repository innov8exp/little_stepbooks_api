package co.botechservices.novlnovl.domain.admin.dict.dto;

import co.botechservices.novlnovl.infrastructure.enums.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortDirectionDto {
    private SortDirection direction;
}
