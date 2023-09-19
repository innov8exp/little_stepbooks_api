package co.botechservices.novlnovl.domain.library.dto;

import co.botechservices.novlnovl.infrastructure.model.BaseDto;
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
