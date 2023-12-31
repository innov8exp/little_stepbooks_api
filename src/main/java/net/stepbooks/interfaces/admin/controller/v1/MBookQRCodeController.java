package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.service.BookQRCodeService;
import net.stepbooks.interfaces.admin.dto.BookQRCodeCreateDto;
import net.stepbooks.interfaces.admin.dto.BookQRCodeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/v1/books-qrcode")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MBookQRCodeController {

    private final BookQRCodeService bookQRCodeService;

    @PostMapping
    public ResponseEntity<?> createBookQRCodes(@RequestBody BookQRCodeCreateDto createDto) throws IOException {
        bookQRCodeService.createBookQRCode(createDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<BookQRCodeDto>> getBookQRCodes(@RequestParam(required = false) String bookId,
                                                              @RequestParam(required = false) String qrcode) {
        List<BookQRCodeDto> list = bookQRCodeService.listByBookId(bookId, qrcode);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookQRCode(@PathVariable String id) {
        bookQRCodeService.removeById(id);
        return ResponseEntity.ok().build();
    }
}
