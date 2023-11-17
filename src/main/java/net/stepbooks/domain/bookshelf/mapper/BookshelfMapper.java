package net.stepbooks.domain.bookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookshelf.entity.Bookshelf;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookshelfMapper extends BaseMapper<Bookshelf> {

    List<Book> findBooksInBookshelfByUser(@Param("userId") String userId);

}
