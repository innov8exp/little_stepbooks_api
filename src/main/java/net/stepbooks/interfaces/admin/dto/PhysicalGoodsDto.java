package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalGoodsDto extends BaseDto {

    private String name;
    private String description;
    private String imgId;
    private String imgUrl;

}
