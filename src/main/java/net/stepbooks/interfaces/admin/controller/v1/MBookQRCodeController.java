package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookQRCode;
import net.stepbooks.domain.book.service.BookQRCodeService;
import net.stepbooks.interfaces.admin.dto.BookQRCodeCreateDto;
import net.stepbooks.interfaces.admin.dto.BookQRCodeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @GetMapping
    public ResponseEntity<IPage<BookQRCodeDto>> getPagedBookQRCodes(@RequestParam int currentPage,
                                                                    @RequestParam int pageSize,
                                                                    @RequestParam String bookId
    ) throws IOException {
        Page<BookQRCode> page = Page.of(currentPage, pageSize);
        IPage<BookQRCodeDto> bookQRCodes = bookQRCodeService.getPage(page, bookId);
        return ResponseEntity.ok(bookQRCodes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookQRCode(@PathVariable String id) {
        bookQRCodeService.removeById(id);
        return ResponseEntity.ok().build();
    }
}
