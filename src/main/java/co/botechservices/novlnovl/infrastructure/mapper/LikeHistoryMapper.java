package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.history.entity.LikeHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface LikeHistoryMapper extends BaseMapper<LikeHistoryEntity> {

    List<BookDto> findLikeBooksByUser(String userId);
}
