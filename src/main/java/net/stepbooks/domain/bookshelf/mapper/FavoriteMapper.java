package net.stepbooks.domain.bookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.domain.bookshelf.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper extends BaseMapper<FavoriteEntity> {

    List<BookEntity> findBooksFromFavoriteByUserId(@Param("userId") String userId);
}
