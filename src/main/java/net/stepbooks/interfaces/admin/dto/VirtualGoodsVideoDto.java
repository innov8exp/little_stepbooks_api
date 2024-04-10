package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualGoodsVideoDto extends BaseDto {

    private String goodsId;
    private String name;
    private String coverId;
    private String coverUrl;
    private String videoId;
    private String videoUrl;

}
