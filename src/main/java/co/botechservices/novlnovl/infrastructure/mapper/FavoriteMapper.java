package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import co.botechservices.novlnovl.domain.library.entity.FavoriteEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper extends BaseMapper<FavoriteEntity> {

    List<BookEntity> findBooksFromFavoriteByUserId(@Param("userId") String userId);
}
