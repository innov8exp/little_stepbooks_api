package net.stepbooks.domain.history.mapper;

import net.stepbooks.interfaces.client.dto.BookDto;
import net.stepbooks.domain.history.entity.LikeHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface LikeHistoryMapper extends BaseMapper<LikeHistoryEntity> {

    List<BookDto> findLikeBooksByUser(String userId);
}
