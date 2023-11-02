package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.book.entity.BookClassificationRefEntity;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.book.mapper.BookClassificationRefMapper;
import net.stepbooks.domain.book.mapper.BookMapper;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.media.service.FileService;
import net.stepbooks.domain.price.entity.PriceEntity;
import net.stepbooks.domain.price.mapper.PriceMapper;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.enums.OrderByCriteria;
import net.stepbooks.interfaces.admin.dto.MBookQueryDto;
import net.stepbooks.interfaces.client.dto.BookDetailDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    public static final String STORE_PATH = "book-assets/images/";
    private static final int TOP = 5;
    private final BookMapper bookMapper;
    private final BookClassificationRefMapper bookClassificationRefMapper;
    private final FileService publicFileServiceImpl;
    private final PriceMapper priceMapper;
    @Value("${aws.cdn}")
    private String cdnUrl;


    @Override
    public BookEntity findBookById(String bookId) {
        return bookMapper.selectOne(Wrappers.<BookEntity>lambdaQuery().eq(BookEntity::getId, bookId));
    }

    @Override
    public BookDetailDto findBookDetailById(String id, String userId) {
        return bookMapper.findBookDetail(id, userId);
    }

    @Override
    public IPage<BookDetailDto> searchBooksWithKeyword(Page<BookDetailDto> page, String keyword) {
        return this.bookMapper.searchAllByBookNameAndKeywords(page, keyword);
    }

    @Override
    public List<BookDetailDto> findTopBooks(OrderByCriteria orderByCriteria, String categoryID) {
        Page<BookDetailDto> page = Page.of(1, TOP);
        return switch (orderByCriteria) {
            case HIGH_RATED -> {
                IPage<BookDetailDto> topHighRatedBooks = bookMapper.findTopHighRatedBooks(page, categoryID);
                yield topHighRatedBooks.getRecords();
            }
            case HIGH_VIEW -> {
                IPage<BookDetailDto> topHighViewedBooks = bookMapper.findTopHighViewedBooks(page, categoryID);
                yield topHighViewedBooks.getRecords();
            }
            case LATEST_CREATED -> {
                IPage<BookDetailDto> topLatestBooks = bookMapper.findTopLatestBooks(page, categoryID);
                yield topLatestBooks.getRecords();
            }
            default -> {
                IPage<BookDetailDto> topDefaultBooks = bookMapper.findTopDefaultBooks(page, categoryID);
                yield topDefaultBooks.getRecords();
            }
        };
    }

    @Override
    public IPage<BookDetailDto> findBooksInPagingByCategory(Page<BookDetailDto> page, OrderByCriteria orderByCriteria, String categoryID) {
        return switch (orderByCriteria) {
            case HIGH_RATED -> bookMapper.findTopHighRatedBooks(page, categoryID);
            case HIGH_VIEW -> bookMapper.findTopHighViewedBooks(page, categoryID);
            case LATEST_CREATED -> bookMapper.findTopLatestBooks(page, categoryID);
            default -> bookMapper.findTopDefaultBooks(page, categoryID);
        };
    }

    @Override
    public IPage<BookEntity> findBooksInPagingByCriteria(Page<BookEntity> page, MBookQueryDto queryDto) {
        return bookMapper.findAllByCriteria(page, queryDto.getBookName(), queryDto.getAuthor());
    }

    @Override
    public IPage<BookDetailDto> searchBookDetailsInPaging(Page<BookDetailDto> page, MBookQueryDto queryDto) {
        return bookMapper.searchBookDetails(page, queryDto.getBookName(), queryDto.getAuthor());
    }

    @Transactional
    @Override
    public void createBook(BookDetailDto bookDetailDto) {
        BookEntity bookEntity = BaseAssembler.convert(bookDetailDto, BookEntity.class);
        bookEntity.setCreatedAt(LocalDateTime.now());
        bookMapper.insert(bookEntity);
        PriceEntity priceEntity = BaseAssembler.convert(bookDetailDto, PriceEntity.class);
        priceEntity.setBookId(bookEntity.getId());
        priceEntity.setCreatedAt(LocalDateTime.now());
        priceMapper.insert(priceEntity);
        String[] categories = bookDetailDto.getCategories();
        for (String category : categories) {
            BookClassificationRefEntity bookCategoryRefEntity = BookClassificationRefEntity.builder()
                    .bookId(bookEntity.getId())
                    .classificationId(category)
                    .build();
            bookClassificationRefMapper.insert(bookCategoryRefEntity);
        }
    }

    @Transactional
    @Override
    public void updateBook(String id, BookDetailDto bookDetailDto) {
        bookDetailDto.setCreatedAt(LocalDateTime.now());
        BookEntity bookEntity = bookMapper.selectById(id);
        log.debug("bookEntity: {}", bookEntity);
        log.debug("bookDetailDto: {}", bookDetailDto);
        BeanUtils.copyProperties(bookDetailDto, bookEntity, "createdAt");
        bookEntity.setModifiedAt(LocalDateTime.now());
        bookEntity.setId(id);
        log.debug("bookEntity-updated: {}", bookEntity);
        bookMapper.updateById(bookEntity);

        PriceEntity priceEntity = priceMapper.selectOne(Wrappers.<PriceEntity>lambdaQuery().eq(PriceEntity::getBookId, id));
        if (ObjectUtils.isEmpty(priceEntity)) {
            priceEntity = new PriceEntity();
            BeanUtils.copyProperties(bookDetailDto, priceEntity, "id");
            priceEntity.setBookId(id);
            priceMapper.insert(priceEntity);
        } else {
            BeanUtils.copyProperties(bookDetailDto, priceEntity, "id", "createdAt");
            priceEntity.setModifiedAt(LocalDateTime.now());
            priceMapper.updateById(priceEntity);
        }
        List<BookClassificationRefEntity> bookCategoryRefEntities = bookClassificationRefMapper.selectList(Wrappers
                .<BookClassificationRefEntity>lambdaQuery()
                .eq(BookClassificationRefEntity::getBookId, id));
        List<String> ids = bookCategoryRefEntities.stream()
                .map(BookClassificationRefEntity::getId).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(ids)) {
            bookClassificationRefMapper.deleteBatchIds(ids);
        }
        String[] categories = bookDetailDto.getCategories();
        for (String category : categories) {
            BookClassificationRefEntity bookCategoryRefEntity = BookClassificationRefEntity.builder()
                    .bookId(id)
                    .classificationId(category)
                    .build();
            bookClassificationRefMapper.insert(bookCategoryRefEntity);
        }
    }

    @Override
    public void deleteBook(String id) {
        bookMapper.deleteById(id);
    }

    @Override
    public BookDetailDto findBook(String id) {
        PriceEntity priceEntity = priceMapper.selectOne(Wrappers.<PriceEntity>lambdaQuery()
                .eq(PriceEntity::getBookId, id));
        BookEntity bookEntity = bookMapper.findBookById(id);
        BookDetailDto bookDetailDto = new BookDetailDto();
        BeanUtils.copyProperties(bookEntity, bookDetailDto);
        if (ObjectUtils.isEmpty(priceEntity)) {
            return bookDetailDto;
        }
        BeanUtils.copyProperties(priceEntity, bookDetailDto);
        return bookDetailDto;
    }

    @Override
    public String uploadCoverImg(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String key = publicFileServiceImpl.upload(file, filename, STORE_PATH);
        return cdnUrl + "/" + key;
    }
}
