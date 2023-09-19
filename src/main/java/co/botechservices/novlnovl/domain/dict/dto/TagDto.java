package co.botechservices.novlnovl.domain.dict.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
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
