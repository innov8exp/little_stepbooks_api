package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualCategoryDto extends BaseDto {

    private String name;
    private String coverId;
    private String coverUrl;

}
