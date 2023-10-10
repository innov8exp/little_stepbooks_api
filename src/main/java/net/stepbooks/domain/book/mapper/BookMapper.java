package net.stepbooks.domain.book.mapper;

import net.stepbooks.interfaces.client.dto.BookDetailDto;
import net.stepbooks.domain.book.entity.BookEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface BookMapper extends BaseMapper<BookEntity> {

    IPage<BookDetailDto> searchAllByBookNameAndKeywords(Page<BookDetailDto> page, @Param("keyword") String keyword);

    IPage<BookDetailDto> findTopDefaultBooks(Page<BookDetailDto> page, @Param("categoryID") String categoryID);

    IPage<BookDetailDto> findTopHighRatedBooks(Page<BookDetailDto> page, @Param("categoryID") String categoryID);

    IPage<BookDetailDto> findTopHighViewedBooks(Page<BookDetailDto> page, @Param("categoryID") String categoryID);

    IPage<BookDetailDto> findTopLatestBooks(Page<BookDetailDto> page, @Param("categoryID") String categoryID);

    IPage<BookEntity> findAllByCriteria(Page<BookEntity> page, @Param("bookName") String bookName,
                                        @Param("author") String author);

    BookEntity findBookById(String id);

    IPage<BookDetailDto> searchBookDetails(Page<BookDetailDto> page, String bookName, String author);

    BookDetailDto findBookDetail(String id, String userId);
}
