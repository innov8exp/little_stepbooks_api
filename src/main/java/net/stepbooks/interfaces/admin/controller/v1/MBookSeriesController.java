package net.stepbooks.interfaces.admin.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookSeries;
import net.stepbooks.domain.book.service.BookSeriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/bookseries")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MBookSeriesController {

    private final BookSeriesService bookSeriesService;

    @Operation(summary = "获取全部系列")
    @GetMapping
    public ResponseEntity<List<BookSeries>> list() {
        List<BookSeries> bookSeries = bookSeriesService.list();
        return ResponseEntity.ok(bookSeries);
    }
}
