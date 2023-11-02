package net.stepbooks.application.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class RatingDto extends BaseDto {
    private Integer rating;
    private String bookId;
    private String userId;
}
