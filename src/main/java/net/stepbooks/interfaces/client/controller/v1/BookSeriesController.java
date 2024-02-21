package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookSeries;
import net.stepbooks.domain.book.service.BookSeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "BookSeries", description = "书籍系列相关接口")
@RestController
@RequestMapping("/v1/bookseries")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class BookSeriesController {

    private final BookSeriesService bookSeriesService;

    @Operation(summary = "获取系列详情")
    @GetMapping("/{id}")
    public ResponseEntity<BookSeries> findBookSeries(@PathVariable String id) {
        BookSeries book = bookSeriesService.getById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "获取全部系列")
    @GetMapping
    public ResponseEntity<List<BookSeries>> list() {
        List<BookSeries> bookSeries = bookSeriesService.list();
        return ResponseEntity.ok(bookSeries);
    }


}
