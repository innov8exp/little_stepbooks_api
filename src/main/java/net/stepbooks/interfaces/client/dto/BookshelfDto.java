package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.model.BaseDto;
import lombok.*;

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
