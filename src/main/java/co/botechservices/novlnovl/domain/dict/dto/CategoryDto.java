package co.botechservices.novlnovl.domain.dict.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
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
