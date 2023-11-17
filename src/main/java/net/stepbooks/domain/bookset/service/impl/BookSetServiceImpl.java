package net.stepbooks.domain.bookset.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.entity.BookSetBook;
import net.stepbooks.domain.bookset.mapper.BookSetMapper;
import net.stepbooks.domain.bookset.service.BookSetBookService;
import net.stepbooks.domain.bookset.service.BookSetService;
import net.stepbooks.domain.product.entity.Product;
import net.stepbooks.domain.product.service.ProductService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
import net.stepbooks.interfaces.admin.dto.BookSetDto;
import net.stepbooks.interfaces.admin.dto.BookSetFormDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookSetServiceImpl extends ServiceImpl<BookSetMapper, BookSet> implements BookSetService {

    private final BookSetBookService bookSetBookService;
    private final BookSetMapper bookSetMapper;
    private final ProductService productService;

    @Override
    public IPage<BookSet> findInPagingByCriteria(Page<BookSet> page, String name) {
        return bookSetMapper.findInPagingByCriteria(page, name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createBookSet(BookSetFormDto bookSet) {
        BookSet newBookSet = BookSet.builder()
                .code(IdWorker.getIdStr())
                .name(bookSet.getName())
                .description(bookSet.getDescription())
                .build();
        save(newBookSet);
        List<BookSetBook> bookSetBooks = Arrays.stream(bookSet.getBookIds()).map(bookId -> BookSetBook.builder()
                .bookSetId(newBookSet.getId())
                .bookId(bookId)
                .build()).toList();
        bookSetBookService.saveBatch(bookSetBooks);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBookSet(BookSetFormDto bookSetFormDto) {
        BookSet bookSet = BaseAssembler.convert(bookSetFormDto, BookSet.class);
        updateById(bookSet);
        bookSetBookService.remove(Wrappers.<BookSetBook>lambdaQuery().eq(BookSetBook::getBookSetId, bookSet.getId()));
        List<BookSetBook> bookSetBooks = Arrays.stream(bookSetFormDto.getBookIds()).map(bookId -> BookSetBook.builder()
                .bookSetId(bookSet.getId())
                .bookId(bookId)
                .build()).toList();
        bookSetBookService.saveBatch(bookSetBooks);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBookSet(String id) {
        boolean exists = productService.exists(Wrappers.<Product>lambdaQuery()
                .eq(Product::getBookSetId, id));
        if (exists) {
            throw new BusinessException(ErrorCode.BOOK_SET_HAS_BEEN_USED);
        }
        bookSetBookService.remove(Wrappers.<BookSetBook>lambdaQuery().eq(BookSetBook::getBookSetId, id));
        bookSetMapper.deleteById(id);
    }

    @Override
    public List<Book> findBooksByBookSetId(String id) {
        return bookSetMapper.findBooksByBookSetId(id);
    }

    @Override
    public BookSetDto findById(String id) {
        BookSet bookSet = getById(id);
        List<String> bookIds = bookSetBookService.list(Wrappers.<BookSetBook>lambdaQuery()
                .eq(BookSetBook::getBookSetId, id)).stream().map(BookSetBook::getBookId).toList();
        BookSetDto bookSetDto = BaseAssembler.convert(bookSet, BookSetDto.class);
        bookSetDto.setBookIds(bookIds);
        return bookSetDto;
    }

    @Override
    public BookSet findByCode(String bookSetCode) {
        return getOne(Wrappers.<BookSet>lambdaQuery().eq(BookSet::getCode, bookSetCode));
    }

}
