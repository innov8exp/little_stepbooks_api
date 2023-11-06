package net.stepbooks.domain.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.history.entity.LikeHistoryEntity;
import net.stepbooks.application.dto.admin.BookDto;

import java.util.List;

public interface LikeHistoryMapper extends BaseMapper<LikeHistoryEntity> {

    List<BookDto> findLikeBooksByUser(String userId);
}