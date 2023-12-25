package net.stepbooks.domain.bookshelf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.bookshelf.entity.Bookshelf;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;
import net.stepbooks.domain.bookshelf.mapper.BookshelfMapper;
import net.stepbooks.domain.bookshelf.service.BookshelfAddLogService;
import net.stepbooks.domain.bookshelf.service.BookshelfService;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.domain.order.service.OrderService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.domain.recommendation.mapper.RecommendationMapper;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.Material;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.client.dto.BookAndMaterialsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookshelfServiceImpl extends ServiceImpl<BookshelfMapper, Bookshelf> implements BookshelfService {

    private final BookshelfMapper bookshelfMapper;
    private final RecommendationMapper recommendationMapper;
    private final BookshelfAddLogService bookshelfAddLogService;
    private final ProductService productService;
    private final OrderService physicalOrderServiceImpl;
    private final OrderOpsService orderOpsService;
    private final BookService bookService;

    @Override
    public Bookshelf findBookInBookshelf(String bookId, String userId) {
        return bookshelfMapper.selectOne(Wrappers.<Bookshelf>lambdaQuery()
                .eq(Bookshelf::getBookId, bookId)
                .eq(Bookshelf::getUserId, userId));
    }

    @Override
    public int addBookToBookshelf(String bookId, String userId) {
        Bookshelf bookInBookshelf = findBookInBookshelf(bookId, userId);
        if (bookInBookshelf != null) {
            return 0;
        }
        Bookshelf bookshelf = Bookshelf.builder().bookId(bookId).userId(userId).build();
        return bookshelfMapper.insert(bookshelf);
    }

    @Override
    public int removeBooksFromBookshelf(List<String> bookIds, String userId) {
        return bookshelfMapper.delete(Wrappers.<Bookshelf>lambdaQuery()
                .and(q -> q.in(Bookshelf::getBookId, bookIds)
                        .eq(Bookshelf::getUserId, userId)));
    }

    @Override
    public List<Book> listBooksInBookshelf(String userId) {
        return bookshelfMapper.findBooksInBookshelfByUser(userId);
    }

    @Transactional
    @Override
    public void setTopBooksFromBookshelf(List<String> bookIds, String userId) {
        List<Bookshelf> bookshelfEntities = bookshelfMapper.selectList(Wrappers.<Bookshelf>lambdaQuery()
                .eq(Bookshelf::getUserId, userId));
        bookshelfEntities.forEach(bookshelf -> {
            bookshelf.setSortIndex(bookshelf.getSortIndex() + bookIds.size());
            bookshelfMapper.updateById(bookshelf);
        });
        List<Bookshelf> topFavoriteEntities = bookshelfMapper.selectList(Wrappers.<Bookshelf>lambdaQuery()
                .eq(Bookshelf::getUserId, userId)
                .in(Bookshelf::getBookId, bookIds)
                .orderByAsc(Bookshelf::getSortIndex));
        for (int i = 0; i < topFavoriteEntities.size(); i++) {
            Bookshelf bookshelf = topFavoriteEntities.get(i);
            bookshelf.setSortIndex(i);
            bookshelfMapper.updateById(bookshelf);
        }
    }

    @Override
    public boolean existsBookInBookshelf(String bookId, String userId) {
        return exists(Wrappers.<Bookshelf>lambdaQuery().eq(Bookshelf::getBookId, bookId)
                .eq(Bookshelf::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeBook(String bookId, String userId) {
        // 激活书籍到书架中
        Bookshelf bookshelf = Bookshelf.builder().bookId(bookId).userId(userId).build();
        save(bookshelf);
        BookshelfAddLog addLog = BookshelfAddLog.builder()
                .bookId(bookId)
                .userId(userId)
                .build();
        bookshelfAddLogService.save(addLog);
    }

    @Override
    public BookAndMaterialsDto getBookAndMaterialsDto(String bookId, String userId) {
        BookDto book = bookService.findBookById(bookId);
        BookAndMaterialsDto bookAndMaterials = BaseAssembler.convert(book, BookAndMaterialsDto.class);
        Set<Material> mergedMaterials = new HashSet<>();
        // 根据bookId获取订单中的产品信息
        List<Product> products = physicalOrderServiceImpl.findOrderProductByUserIdAndBookId(userId, bookId);
        products.forEach(product -> {
            Material[] materials = product.getParsedMaterials();
            mergedMaterials.addAll(Arrays.asList(materials));
        });
        bookAndMaterials.setMaterials(mergedMaterials.toArray(new Material[0]));
        return bookAndMaterials;
    }
}
