package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.book.dto.BookDto;
import co.botechservices.novlnovl.domain.history.entity.FinishHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinishHistoryMapper extends BaseMapper<FinishHistoryEntity> {

    List<BookDto> findFinishBooksByUser(@Param("userId") String userId);
}
