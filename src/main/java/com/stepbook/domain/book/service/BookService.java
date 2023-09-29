package com.stepbook.domain.book.service;

import com.stepbook.domain.admin.book.dto.MBookQueryDto;
import com.stepbook.domain.book.dto.BookDetailDto;
import com.stepbook.domain.book.entity.BookEntity;
import com.stepbook.infrastructure.enums.BookStatus;
import com.stepbook.infrastructure.enums.OrderByCriteria;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    BookEntity findBookById(String bookId);

    BookDetailDto findBookDetailById(String id, String userId);

    IPage<BookDetailDto> searchBooksWithKeyword(Page<BookDetailDto> page, String keyword);

    List<BookDetailDto> findTopBooks(OrderByCriteria orderByCriteria, String categoryID);

    IPage<BookDetailDto> findBooksInPagingByCategory(Page<BookDetailDto> page, OrderByCriteria orderByCriteria, String categoryID);

    IPage<BookEntity> findBooksInPagingByCriteria(Page<BookEntity> page, MBookQueryDto queryDto);

    IPage<BookDetailDto> searchBookDetailsInPaging(Page<BookDetailDto> page, MBookQueryDto queryDto);

    void createBook(BookDetailDto bookDetailDto);

    void updateBook(String id, BookDetailDto bookDetailDto);

    void deleteBook(String id);

    BookDetailDto findBook(String id);

    void updateBookStatus(String id, BookStatus bookStatus);

    String uploadCoverImg(MultipartFile file);

//    List<BookEntity> findHighViewTopBooks();
//
//    List<BookEntity> findHighViewTopBooksInPaging();
//
//    List<BookEntity> findLatestCreatedTopBooks();
//
//    List<BookEntity> findLatestCreatedTopBooks();
//
//    List<BookEntity> findDefaultTopBooks();


}
