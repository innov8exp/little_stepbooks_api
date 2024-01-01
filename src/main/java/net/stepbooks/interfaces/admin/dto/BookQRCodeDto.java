package net.stepbooks.interfaces.admin.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class BookQRCodeDto {
    private String bookId;
    private String qrCode;
    private String url;
}
