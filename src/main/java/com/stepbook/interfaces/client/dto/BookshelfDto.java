package com.stepbook.interfaces.client.dto;

import com.stepbook.infrastructure.model.BaseDto;
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
