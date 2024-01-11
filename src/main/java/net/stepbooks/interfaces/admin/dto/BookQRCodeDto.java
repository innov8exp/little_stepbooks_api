package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import lombok.experimental.Accessors;
import net.stepbooks.domain.book.enums.BookActiveStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class BookQRCodeDto {
    private String id;
    private String bookId;
    private String qrCode;
    private BookActiveStatus activeStatus;
    private String qrCodeUrl;
}
