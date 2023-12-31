package net.stepbooks.interfaces.client.dto;

import lombok.*;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookshelfDto extends BaseDto {
    private String bookId;
    private String userId;
    private Integer sortIndex;
}
