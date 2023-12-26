package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.stepbooks.domain.book.entity.BookQRCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookQRCodeCreateDto {
    private String bookId;
    private int size;
}
