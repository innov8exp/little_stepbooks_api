package co.botechservices.novlnovl.domain.comment.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RatingDto extends BaseDto {
    private Integer rating;
    private String bookId;
    private String userId;
}
