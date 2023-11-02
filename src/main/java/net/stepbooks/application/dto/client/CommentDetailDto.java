package net.stepbooks.application.dto.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseDto;

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
