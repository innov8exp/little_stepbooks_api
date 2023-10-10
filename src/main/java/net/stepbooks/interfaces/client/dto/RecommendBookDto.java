package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.enums.RecommendType;
import net.stepbooks.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendBookDto extends BaseDto {

    private String bookName;
    private String author;
    private String coverImg;
    private String introduction;
    private Boolean isSerialized;
    private Boolean hasEnding;
    private RecommendType recommendType;
    private Integer sortIndex;
}
