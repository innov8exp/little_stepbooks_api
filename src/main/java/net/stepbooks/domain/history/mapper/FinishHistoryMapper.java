package net.stepbooks.domain.history.mapper;

import net.stepbooks.interfaces.client.dto.BookDto;
import net.stepbooks.domain.history.entity.FinishHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinishHistoryMapper extends BaseMapper<FinishHistoryEntity> {

    List<BookDto> findFinishBooksByUser(@Param("userId") String userId);
}
