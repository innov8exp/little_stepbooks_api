package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.model.BaseDto;
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
