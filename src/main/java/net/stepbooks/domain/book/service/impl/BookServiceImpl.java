package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.book.entity.*;
import net.stepbooks.domain.book.mapper.BookChapterMapper;
import net.stepbooks.domain.book.mapper.BookMapper;
import net.stepbooks.domain.book.mapper.BookQRCodeMapper;
import net.stepbooks.domain.book.service.BookClassificationService;
import net.stepbooks.domain.book.service.BookMediaService;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.classification.entity.Classification;
import net.stepbooks.domain.media.service.MediaService;
import net.stepbooks.domain.media.service.impl.PrivateFileServiceImpl;
import net.stepbooks.domain.order.service.OrderOpsService;
import net.stepbooks.infrastructure.assembler.BaseAssembler;
import net.stepbooks.infrastructure.exception.BusinessException;
import net.stepbooks.infrastructure.exception.ErrorCode;
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
    private final PrivateFileServiceImpl privateFileService;
    private final OrderOpsService orderOpsService;
    private final MediaService mediaService;
    private final BookMediaService bookMediaService;
    private final BookQRCodeMapper bookQRCodeMapper;

    @Override
    public IPage<BookDto> findBooksInPagingByCriteria(Page<BookDto> page, MBookQueryDto queryDto) {
        return bookMapper.findAllByCriteria(page, queryDto.getBookName(), queryDto.getAuthor());
    }

    @Transactional(rollbackFor = Exception.class)
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
        List<BookMedia> productMedias = bookDto.getMedias().stream().peek(bookMedia ->
                bookMedia.setBookId(book.getId())).toList();
        bookMediaService.saveBatch(productMedias);
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
        bookMediaService.remove(Wrappers.<BookMedia>lambdaQuery().eq(BookMedia::getBookId, id));
        List<BookMedia> productMedias = bookDto.getMedias().stream().peek(bookMedia ->
                bookMedia.setBookId(id)).toList();
        bookMediaService.saveBatch(productMedias);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(String id) {
        bookClassificationService.remove(Wrappers.<BookClassification>lambdaQuery()
                .eq(BookClassification::getBookId, id));
        bookMapper.deleteById(id);
        bookMediaService.remove(Wrappers.<BookMedia>lambdaQuery().eq(BookMedia::getBookId, id));
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
        BookDto book = bookMapper.findBookById(bookId);
        List<BookMedia> bookMedias = bookMediaService.list(Wrappers.<BookMedia>lambdaQuery()
                .eq(BookMedia::getBookId, bookId));
        book.setMedias(bookMedias);
        return book;
    }

    @Override
    public List<Book> findBooksByProductId(String productId) {
        return bookMapper.findBooksByProductId(productId);
    }

    @Override
    public List<BookChapter> getBookChaptersByUser(String userId, String bookId) {
        boolean exists = orderOpsService.checkBookInUserOrder(userId, bookId);
        if (!exists) {
            throw new BusinessException(ErrorCode.BOOK_NOT_EXISTS_IN_ORDER_ERROR);
        }
        List<BookChapter> bookChapters = chapterMapper.selectList(Wrappers.<BookChapter>lambdaQuery()
                .eq(BookChapter::getBookId, bookId));
        return bookChapters.stream().peek(bookChapter -> {
            String imgId = bookChapter.getImgId();
            String audioId = bookChapter.getAudioId();
            String imgKey = mediaService.getById(imgId).getObjectKey();
            String audioKey = mediaService.getById(audioId).getObjectKey();
            String imgUrl = privateFileService.getUrl(imgKey);
            String audioUrl = privateFileService.getUrl(audioKey);
            bookChapter.setImgUrl(imgUrl);
            bookChapter.setAudioUrl(audioUrl);
        }).toList();
    }

    @Override
    public Book findBookByQRCode(String qrCode) {
        BookQRCode bookQRCode = bookQRCodeMapper
                .selectOne(Wrappers.<BookQRCode>lambdaQuery().eq(BookQRCode::getQrCode, qrCode));
        if (bookQRCode != null) {
            return bookMapper.selectById(bookQRCode.getBookId());
        }
        return null;
    }
}
