package com.stepbook.domain.book.service.impl;

import com.stepbook.interfaces.admin.dto.MBookQueryDto;
import com.stepbook.interfaces.client.dto.BookDetailDto;
import com.stepbook.domain.book.entity.BookCategoryRefEntity;
import com.stepbook.domain.book.entity.BookEntity;
import com.stepbook.domain.book.service.BookService;
import com.stepbook.domain.common.service.FileService;
import com.stepbook.domain.price.entity.PriceEntity;
import com.stepbook.infrastructure.assembler.BaseAssembler;
import com.stepbook.infrastructure.enums.BookStatus;
import com.stepbook.infrastructure.enums.OrderByCriteria;
import com.stepbook.domain.book.mapper.BookCategoryRefMapper;
import com.stepbook.domain.book.mapper.BookMapper;
import com.stepbook.domain.price.mapper.PriceMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    public static final String STORE_PATH = "book-assets/images/";
    private static final int TOP = 5;
    private final BookMapper bookMapper;
    private final FileService fileService;
    private final BookCategoryRefMapper bookCategoryRefMapper;
    private final PriceMapper priceMapper;
    @Value("${aws.cdn}")
    private String cdnUrl;


    public BookServiceImpl(BookMapper bookMapper, FileService fileService,
                           BookCategoryRefMapper bookCategoryRefMapper, PriceMapper priceMapper) {
        this.bookMapper = bookMapper;
        this.fileService = fileService;
        this.bookCategoryRefMapper = bookCategoryRefMapper;
        this.priceMapper = priceMapper;
    }

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
        switch (orderByCriteria) {
            case HIGH_RATED:
                IPage<BookDetailDto> topHighRatedBooks = bookMapper.findTopHighRatedBooks(page, categoryID);
                return topHighRatedBooks.getRecords();
            case HIGH_VIEW:
                IPage<BookDetailDto> topHighViewedBooks = bookMapper.findTopHighViewedBooks(page, categoryID);
                return topHighViewedBooks.getRecords();
            case LATEST_CREATED:
                IPage<BookDetailDto> topLatestBooks = bookMapper.findTopLatestBooks(page, categoryID);
                return topLatestBooks.getRecords();
            default:
                IPage<BookDetailDto> topDefaultBooks = bookMapper.findTopDefaultBooks(page, categoryID);
                return topDefaultBooks.getRecords();
        }
    }

    @Override
    public IPage<BookDetailDto> findBooksInPagingByCategory(Page<BookDetailDto> page, OrderByCriteria orderByCriteria, String categoryID) {
        switch (orderByCriteria) {
            case HIGH_RATED:
                return bookMapper.findTopHighRatedBooks(page, categoryID);
            case HIGH_VIEW:
                return bookMapper.findTopHighViewedBooks(page, categoryID);
            case LATEST_CREATED:
                return bookMapper.findTopLatestBooks(page, categoryID);
            default:
                return bookMapper.findTopDefaultBooks(page, categoryID);
        }
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
            BookCategoryRefEntity bookCategoryRefEntity = BookCategoryRefEntity.builder()
                    .bookId(bookEntity.getId())
                    .categoryId(category)
                    .build();
            bookCategoryRefMapper.insert(bookCategoryRefEntity);
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
        List<BookCategoryRefEntity> bookCategoryRefEntities = bookCategoryRefMapper.selectList(Wrappers
                .<BookCategoryRefEntity>lambdaQuery()
                .eq(BookCategoryRefEntity::getBookId, id));
        List<String> ids = bookCategoryRefEntities.stream()
                .map(BookCategoryRefEntity::getId).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(ids)) {
            bookCategoryRefMapper.deleteBatchIds(ids);
        }
        String[] categories = bookDetailDto.getCategories();
        for (String category : categories) {
            BookCategoryRefEntity bookCategoryRefEntity = BookCategoryRefEntity.builder()
                    .bookId(id)
                    .categoryId(category)
                    .build();
            bookCategoryRefMapper.insert(bookCategoryRefEntity);
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
    public void updateBookStatus(String id, BookStatus bookStatus) {
        BookEntity bookEntity = bookMapper.selectById(id);
        bookEntity.setStatus(bookStatus.name());
        bookMapper.updateById(bookEntity);
    }

    @Override
    public String uploadCoverImg(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        String key = fileService.upload(file, STORE_PATH + UUID.randomUUID() + "." + fileType);
        return cdnUrl + "/" + key;
    }
}
