package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDto extends BaseDto {
    private String bookId;
    private String userId;
    private String content;
}
