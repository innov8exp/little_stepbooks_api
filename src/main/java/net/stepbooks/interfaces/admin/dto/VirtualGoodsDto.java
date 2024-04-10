package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualGoodsDto extends BaseDto {

    private String name;
    private String description;
    private String categoryId;
    private int toAddMonth;

}
