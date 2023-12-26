package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import net.stepbooks.domain.book.entity.BookQRCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookQRCodeDto {
    private BookQRCode bookQRCode;
    private byte[] qrImageData;
}
