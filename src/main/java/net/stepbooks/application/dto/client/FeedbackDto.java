package net.stepbooks.application.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseDto;


@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackDto extends BaseDto {
    private String userId;
    @NotBlank
    private String content;
}
