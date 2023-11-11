package net.stepbooks.interfaces.admin.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.service.BookSetService;
import net.stepbooks.interfaces.admin.dto.BookSetFormDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/v1/book-sets")
@RequiredArgsConstructor
@SecurityRequirement(name = "Admin Authentication")
public class MBookSetController {

    private final BookSetService bookSetService;

    @PostMapping
    public ResponseEntity<?> createBookSet(@RequestBody BookSetFormDto bookSet) {
        bookSetService.createBookSet(bookSet);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookSet(@PathVariable String id, @RequestBody BookSet bookSet) {
        bookSet.setId(id);
        bookSetService.updateById(bookSet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookSet(@PathVariable String id) {
        bookSetService.deleteBookSet(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<IPage<BookSet>> getPagedBookSets(@RequestParam int currentPage,
                                              @RequestParam int pageSize,
                                              @RequestParam(required = false) String name) {
        Page<BookSet> page = Page.of(currentPage, pageSize);
        IPage<BookSet> bookSets = bookSetService.findInPagingByCriteria(page, name);
        return ResponseEntity.ok(bookSets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookSetFormDto> getBookSet(@PathVariable String id) {
        BookSetFormDto bookSet = bookSetService.getBookSet(id);
        return ResponseEntity.ok(bookSet);
    }
}


