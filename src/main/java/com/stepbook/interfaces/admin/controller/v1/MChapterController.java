package com.stepbook.interfaces.admin.controller.v1;

import com.stepbook.interfaces.admin.dto.PaymentTypeDto;
import com.stepbook.interfaces.client.dto.ChapterDto;
import com.stepbook.domain.book.entity.ChapterEntity;
import com.stepbook.domain.book.service.ChapterService;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import com.stepbook.infrastructure.enums.UploadType;
import com.stepbook.infrastructure.exception.BusinessException;
import com.stepbook.infrastructure.exception.ErrorCode;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;
import net.sf.jmimemagic.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/admin/v1/chapters")
public class MChapterController {

    private final ChapterService chapterService;

    public MChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping
    public ResponseEntity<?> createChapter(@RequestBody ChapterDto chapterDto) {
        chapterService.createChapter(chapterDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateChapter(@PathVariable String id, @RequestBody ChapterDto chapterDto) {
        chapterService.updateChapter(id, chapterDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/payment-type")
    public ResponseEntity<?> updateChapterPaymentType(@PathVariable String id, @RequestBody PaymentTypeDto paymentTypeDto) {
        chapterService.updateChapterType(id, paymentTypeDto.getNeedPay());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChapter(@PathVariable String id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<ChapterDto>> getAllChapters(@RequestParam int currentPage,
                                                            @RequestParam int pageSize,
                                                            @RequestParam String bookId
    ) {
        Page<ChapterEntity> page = Page.of(currentPage, pageSize);
        IPage<ChapterEntity> chapters = chapterService.findChaptersInPaging(page, bookId);
        IPage<ChapterDto> chapterDtoIPage = chapters.convert(chapterEntity -> BaseAssembler.convert(chapterEntity, ChapterDto.class));
        return ResponseEntity.ok(chapterDtoIPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> getChapter(@PathVariable String id) {
        ChapterEntity chapterEntity = chapterService.findChapterById(id);
        return ResponseEntity.ok(BaseAssembler.convert(chapterEntity, ChapterDto.class));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadChapter(@RequestParam String bookId,
                                           @RequestParam UploadType uploadType,
                                           @RequestParam("file") @NotNull MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            MagicMatch magicMatch = Magic.getMagicMatch(bytes);
            String mimeType = magicMatch.getMimeType();
            if (!MimeTypeUtils.TEXT_PLAIN_VALUE.equals(mimeType)) {
                throw new BusinessException(ErrorCode.FILETYPE_ERROR);
            }
        } catch (IOException | MagicException | MagicParseException | MagicMatchNotFoundException e) {
            e.printStackTrace();
        }
        chapterService.uploadContent(bookId, uploadType, file);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/template/download")
    public ResponseEntity<?> downloadTemplate() {
        InputStreamResource resource = null;
        ClassPathResource classPathResource = new ClassPathResource("template/chapter-template.txt");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            resource = new InputStreamResource(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chapter-template.txt");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<String> readChapterContent(@PathVariable String id) {
        String content = chapterService.readChapter(id);
        return ResponseEntity.ok().body(content);
    }

    @GetMapping("/max-chapter-number")
    public ResponseEntity<Integer> getMaxChapterNumber(@RequestParam String bookId) {
        Integer maxNumber = chapterService.getMaxChapterNumber(bookId);
        return ResponseEntity.ok(maxNumber);
    }
}
