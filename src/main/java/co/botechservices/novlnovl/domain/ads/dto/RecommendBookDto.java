package co.botechservices.novlnovl.domain.ads.dto;

import co.botechservices.novlnovl.infrastructure.enums.RecommendType;
import co.botechservices.novlnovl.infrastructure.model.BaseDto;
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
