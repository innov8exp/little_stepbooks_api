package co.botechservices.novlnovl.domain.ads.dto;

import co.botechservices.novlnovl.infrastructure.enums.RecommendType;
import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendDto extends BaseDto {
    private String bookId;
    private String bookName;
    private Integer sortIndex;
    private String introduction;
    private RecommendType recommendType;
}
