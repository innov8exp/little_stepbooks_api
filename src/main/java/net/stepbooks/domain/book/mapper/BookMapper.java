package net.stepbooks.domain.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.application.dto.admin.BookDto;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.classification.entity.Classification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper extends BaseMapper<Book> {

    IPage<BookDto> findAllByCriteria(Page<BookDto> page, @Param("bookName") String bookName,
                                     @Param("author") String author);

    List<Classification> findClassificationsByBookId(@Param("bookId") String bookId);

    BookDto findBookById(@Param("bookId") String bookId);
}
