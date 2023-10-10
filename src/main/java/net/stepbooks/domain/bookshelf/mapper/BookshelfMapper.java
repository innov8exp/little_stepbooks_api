package net.stepbooks.domain.bookshelf.mapper;

import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.bookshelf.entity.BookshelfEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookshelfMapper extends BaseMapper<BookshelfEntity> {

    List<BookEntity> findBooksInBookshelfByUser(@Param("userId") String userId);

}
