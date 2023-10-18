package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.enums.RecommendType;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendDto extends BaseDto {
    private String bookId;
    private String bookName;
    private Integer sortIndex;
    private String introduction;
    private RecommendType recommendType;
}
