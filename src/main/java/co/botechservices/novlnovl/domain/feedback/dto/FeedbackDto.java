package co.botechservices.novlnovl.domain.feedback.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackDto extends BaseDto {
    private String userId;
    @NotBlank
    private String content;
}
