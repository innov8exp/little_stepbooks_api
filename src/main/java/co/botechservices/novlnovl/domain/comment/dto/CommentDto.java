package co.botechservices.novlnovl.domain.comment.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDto extends BaseDto {
    private String bookId;
    private String userId;
    private String content;
}
