package co.botechservices.novlnovl.domain.admin.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MBookQueryDto {
    private String bookName;
    private String author;
}
