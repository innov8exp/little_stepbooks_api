package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookshelfAddLogDto extends BaseDto {
    private String userId;
    private String bookSetId;
    private String bookSetCode;
    private String bookId;
    private String bookName;
    private String bookImgUrl;
}
