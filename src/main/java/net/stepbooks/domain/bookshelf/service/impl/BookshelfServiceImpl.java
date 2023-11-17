package net.stepbooks.domain.bookshelf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.entity.BookSetBook;
import net.stepbooks.domain.bookset.service.BookSetBookService;
import net.stepbooks.domain.bookset.service.BookSetService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookshelfServiceImpl extends ServiceImpl<BookshelfMapper, Bookshelf> implements BookshelfService {

    private final BookshelfMapper bookshelfMapper;
    private final RecommendationMapper recommendationMapper;
    private final BookSetService bookSetService;
    private final BookshelfAddLogService bookshelfAddLogService;
    private final ProductService productService;
    private final OrderService physicalOrderServiceImpl;
    private final OrderOpsService orderOpsService;
    private final BookService bookService;
    private final BookSetBookService bookSetBookService;

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
        //        if (ObjectUtils.isEmpty(books)) {
//            return recommendMapper.findDefaultRecommendBooks();
//        }
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
    public boolean existsBookSetInBookshelf(String bookSetCode, String userId) {
        return exists(Wrappers.<Bookshelf>lambdaQuery().eq(Bookshelf::getBookSetCode, bookSetCode)
                .eq(Bookshelf::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeBookSet(String bookSetCode, String userId) {
        BookSet bookSet = bookSetService.findByCode(bookSetCode);
        List<Book> books = bookSetService.findBooksByBookSetId(bookSet.getId());
        // 拆书包，将书包中的书籍添加到书架
        saveBatch(books.stream().map(book -> Bookshelf.builder()
                        .bookSetId(bookSet.getId())
                        .bookSetCode(bookSetCode)
                        .bookId(book.getId())
                        .userId(userId)
                        .build())
                .collect(Collectors.toList()));
        BookshelfAddLog addLog = BookshelfAddLog.builder()
                .bookSetId(bookSet.getId())
                .bookSetCode(bookSetCode)
                .userId(userId)
                .build();
        bookshelfAddLogService.save(addLog);
    }

    @Override
    public BookAndMaterialsDto getBookAndMaterialsDto(String bookId, String userId) {
        BookDto book = bookService.findBookById(bookId);
        BookAndMaterialsDto bookAndMaterials = BaseAssembler.convert(book, BookAndMaterialsDto.class);
        Set<Material> mergedMaterials = new HashSet<>();
        List<BookSetBook> bookSetBooks = bookSetBookService.findByBookId(bookId);
        // 根据激活码获取订单中的产品信息
        List<Product> products = physicalOrderServiceImpl.findOrderProductByUserIdAndBookSetIds(userId,
                bookSetBooks.stream().map(BookSetBook::getBookSetId).collect(Collectors.toSet()));
        products.forEach(product -> {
            Material[] materials = product.getParsedMaterials();
            mergedMaterials.addAll(Arrays.asList(materials));
        });
        bookAndMaterials.setMaterials(mergedMaterials.toArray(new Material[0]));
        return bookAndMaterials;
    }
}
