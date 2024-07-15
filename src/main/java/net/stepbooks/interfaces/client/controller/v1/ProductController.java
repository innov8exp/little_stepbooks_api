package net.stepbooks.interfaces.client.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.course.service.CourseService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.user.entity.User;
import net.stepbooks.infrastructure.enums.StoreType;
import net.stepbooks.infrastructure.util.ContextManager;
import net.stepbooks.interfaces.admin.dto.ProductDto;
import net.stepbooks.interfaces.client.dto.CourseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "SPU", description = "SPU产品相关接口")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;
    private final BookService bookService;
    private final CourseService courseService;
    private final ContextManager contextManager;

    @Operation(summary = "搜索SPU产品，如果不传storeType，默认为REGULAR，如需查询积分商城产品，请设置为POINTS")
    @GetMapping("/search")
    public ResponseEntity<IPage<Product>> searchProducts(@RequestParam int currentPage,
                                                         @RequestParam int pageSize,
                                                         @RequestParam(required = false) String keyword,
                                                         @RequestParam(required = false) String tag,
                                                         @RequestParam(required = false) StoreType storeType) {
        if (storeType == null) {
            storeType = StoreType.REGULAR;
        }
        Page<Product> page = Page.of(currentPage, pageSize);
        IPage<Product> products = productService.searchProducts(storeType, page, tag, keyword);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "获取推荐SPU产品，如果不传storeType，默认为REGULAR，如需查询积分商城产品，请设置为POINTS")
    @GetMapping("/recommend")
    public ResponseEntity<IPage<Product>> getRecommendProducts(@RequestParam int currentPage,
                                                               @RequestParam int pageSize,
                                                               @RequestParam(required = false) StoreType storeType) {
        if (storeType == null) {
            storeType = StoreType.REGULAR;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication)) {
            Page<Product> page = Page.of(currentPage, pageSize);
            IPage<Product> products = productService.listDefaultRecommendProducts(storeType, page);
            return ResponseEntity.ok(products);
        }
        User user = contextManager.currentUser();
        Float childMaxAge = user.getChildMaxAge();
        Float childMinAge = user.getChildMinAge();
        Page<Product> page = Page.of(currentPage, pageSize);
        IPage<Product> products = productService.listRecommendProducts(storeType, page, childMinAge, childMaxAge);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "获取新品SPU", description = "换一批可以传入当前页码和页大小")
    @GetMapping("/new")
    public ResponseEntity<IPage<Product>> getNewProducts(@RequestParam int currentPage,
                                                         @RequestParam int pageSize,
                                                         @RequestParam(required = false) StoreType storeType) {
        if (storeType == null) {
            storeType = StoreType.REGULAR;
        }
        Page<Product> page = Page.of(currentPage, pageSize);
        IPage<Product> products = productService.listNewProducts(storeType, page);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "获取SPU产品详情")
    public ResponseEntity<ProductDto> getProductDetail(@PathVariable String id) {
        ProductDto productDto = productService.findDetailById(id);
        return ResponseEntity.ok(productDto);
    }

    @Deprecated
    @GetMapping("/{id}/books")
    @Operation(summary = "获取SPU产品的书籍")
    public ResponseEntity<List<Book>> getProductBooks(@PathVariable String id) {
        List<Book> books = bookService.findBooksByProductId(id);
        return ResponseEntity.ok(books);
    }

    @Deprecated
    @GetMapping("/{id}/courses")
    @Operation(summary = "获取SPU产品的课程")
    public ResponseEntity<List<CourseDto>> getProductCourses(@PathVariable String id) {
        List<CourseDto> courses = courseService.findCoursesByProductId(id);
        return ResponseEntity.ok(courses);
    }

}
