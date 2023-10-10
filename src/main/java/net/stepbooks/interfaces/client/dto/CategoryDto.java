package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.model.BaseDto;
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
