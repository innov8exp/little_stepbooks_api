package net.stepbooks.domain.book.vo;

import lombok.Data;
import net.stepbooks.domain.book.entity.BookQRCode;

@Data
public class BookQRCodeResponse {
    private BookQRCode bookQRCode;
    private byte[] qrImageData;
}
