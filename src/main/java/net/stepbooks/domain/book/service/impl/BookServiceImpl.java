package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.entity.BookClassification;
import net.stepbooks.domain.book.mapper.BookChapterMapper;
import net.stepbooks.domain.book.mapper.BookMapper;
import net.stepbooks.domain.book.service.BookClassificationService;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.admin.dto.MBookQueryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final BookMapper bookMapper;
    private final BookChapterMapper chapterMapper;
    private final BookClassificationService bookClassificationService;

    @Override
    public IPage<BookDto> findBooksInPagingByCriteria(Page<BookDto> page, MBookQueryDto queryDto) {
        return bookMapper.findAllByCriteria(page, queryDto.getBookName(), queryDto.getAuthor());
    }

    @Transactional
    @Override
    public void createBook(BookDto bookDto) {
        Book book = BaseAssembler.convert(bookDto, Book.class);
        bookMapper.insert(book);
        List<BookClassification> bookClassifications
                = Arrays.stream(bookDto.getClassifications()).map(classification -> BookClassification.builder()
                .bookId(book.getId())
                .classificationId(classification)
                .build()).toList();
        bookClassificationService.saveBatch(bookClassifications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(String id, BookDto bookDto) {
        Book book = BaseAssembler.convert(bookDto, Book.class);
        book.setId(id);
        this.updateById(book);
        bookClassificationService.remove(Wrappers.<BookClassification>lambdaQuery().eq(BookClassification::getBookId, id));
        List<BookClassification> bookClassifications
                = Arrays.stream(bookDto.getClassifications()).map(classification -> BookClassification.builder()
                .bookId(book.getId())
                .classificationId(classification)
                .build()).toList();
        bookClassificationService.saveBatch(bookClassifications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(String id) {
        bookClassificationService.remove(Wrappers.<BookClassification>lambdaQuery()
                .eq(BookClassification::getBookId, id));
        bookMapper.deleteById(id);
    }

    @Override
    public Long chapterCount(String bookId) {
        return chapterMapper.selectCount(Wrappers.<BookChapter>lambdaQuery()
                .eq(BookChapter::getBookId, bookId));
    }

    @Override
    public List<Classification> getBookClassifications(String bookId) {
        return bookMapper.findClassificationsByBookId(bookId);
    }

    @Override
    public BookDto findBookById(String bookId) {
        return bookMapper.findBookById(bookId);
    }

    @Override
    public List<Book> findBooksByProductId(String productId) {
        return bookMapper.findBooksByProductId(productId);
    }
}
