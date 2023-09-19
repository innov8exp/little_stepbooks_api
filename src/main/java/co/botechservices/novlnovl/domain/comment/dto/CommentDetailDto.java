package co.botechservices.novlnovl.domain.comment.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommentDetailDto extends BaseDto {
    private String bookId;
    private String userId;
    private String content;
    private String username;
    private String nickname;
    private String avatarImg;
    private String bookName;
}
