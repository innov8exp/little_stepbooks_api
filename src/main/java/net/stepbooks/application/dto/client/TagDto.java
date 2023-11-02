package net.stepbooks.application.dto.client;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto extends BaseDto {
    private String tagName;
    private String description;
}
