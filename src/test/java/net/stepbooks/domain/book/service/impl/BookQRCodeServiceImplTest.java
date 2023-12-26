package net.stepbooks.domain.book.service.impl;

import jakarta.annotation.Resource;
import net.stepbooks.domain.book.service.BookQRCodeService;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class BookQRCodeServiceImplTest {
    @Resource
    private BookQRCodeService bookQRCodeService;

//    @Test
    void generate() throws IOException {
        byte[] byteArray = bookQRCodeService.generateQRImage("123", UUID.randomUUID().toString());
        try (FileOutputStream fos = new FileOutputStream("./" + System.currentTimeMillis() + ".png")) {
            fos.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
