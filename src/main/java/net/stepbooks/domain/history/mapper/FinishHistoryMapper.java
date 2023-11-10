package net.stepbooks.domain.history.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.history.entity.FinishHistoryEntity;
import net.stepbooks.interfaces.admin.dto.BookDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinishHistoryMapper extends BaseMapper<FinishHistoryEntity> {

    List<BookDto> findFinishBooksByUser(@Param("userId") String userId);
}
