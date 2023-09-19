package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.library.entity.BookshelfEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookshelfMapper extends BaseMapper<BookshelfEntity> {

    List<BookEntity> findBooksInBookshelfByUser(@Param("userId") String userId);

}
