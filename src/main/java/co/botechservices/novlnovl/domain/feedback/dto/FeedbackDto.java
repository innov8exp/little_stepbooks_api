package co.botechservices.novlnovl.domain.feedback.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackDto extends BaseDto {
    private String userId;
    @NotBlank
    private String content;
}
