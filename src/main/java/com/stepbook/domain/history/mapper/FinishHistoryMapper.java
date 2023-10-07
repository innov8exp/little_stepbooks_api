package com.stepbook.domain.history.mapper;

import com.stepbook.interfaces.client.dto.BookDto;
import com.stepbook.domain.history.entity.FinishHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinishHistoryMapper extends BaseMapper<FinishHistoryEntity> {

    List<BookDto> findFinishBooksByUser(@Param("userId") String userId);
}
