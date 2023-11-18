package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.entity.BookClassificationRef;
import net.stepbooks.domain.book.mapper.BookChapterMapper;
import net.stepbooks.domain.book.mapper.BookClassificationRefMapper;
import net.stepbooks.domain.book.mapper.BookMapper;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.media.service.FileService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.admin.dto.MBookQueryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    public static final String STORE_PATH = "book-assets/images/";
    private final BookMapper bookMapper;
    private final BookChapterMapper chapterMapper;
    private final BookClassificationRefMapper bookClassificationRefMapper;
    private final FileService publicFileServiceImpl;
    @Value("${aws.cdn}")
    private String cdnUrl;

    @Override
    public IPage<BookDto> findBooksInPagingByCriteria(Page<BookDto> page, MBookQueryDto queryDto) {
        return bookMapper.findAllByCriteria(page, queryDto.getBookName(), queryDto.getAuthor());
    }

    @Transactional
    @Override
    public void createBook(BookDto bookDto) {
        Book book = BaseAssembler.convert(bookDto, Book.class);
        bookMapper.insert(book);
        String[] classifications = bookDto.getClassifications();
        for (String classification : classifications) {
            BookClassificationRef bookCategoryRef = BookClassificationRef.builder()
                    .bookId(book.getId())
                    .classificationId(classification)
                    .build();
            bookClassificationRefMapper.insert(bookCategoryRef);
        }
    }

    @Transactional
    @Override
    public void updateBook(String id, BookDto bookDto) {
        Book book = this.getById(id);
        log.debug("bookEntity: {}", book);
        BeanUtils.copyProperties(bookDto, book);
        book.setId(id);
        log.debug("bookEntity-updated: {}", book);
        this.updateById(book);

        List<BookClassificationRef> bookCategoryRefEntities = bookClassificationRefMapper.selectList(Wrappers
                .<BookClassificationRef>lambdaQuery()
                .eq(BookClassificationRef::getBookId, id));
        List<String> ids = bookCategoryRefEntities.stream()
                .map(BookClassificationRef::getId).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(ids)) {
            bookClassificationRefMapper.deleteBatchIds(ids);
        }
        String[] categories = bookDto.getClassifications();
        for (String category : categories) {
            BookClassificationRef bookCategoryRefEntity = BookClassificationRef.builder()
                    .bookId(id)
                    .classificationId(category)
                    .build();
            bookClassificationRefMapper.insert(bookCategoryRefEntity);
        }
    }

    @Override
    @Transactional
    public void deleteBook(String id) {
        bookClassificationRefMapper.delete(Wrappers.<BookClassificationRef>lambdaQuery()
                .eq(BookClassificationRef::getBookId, id));
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
}
