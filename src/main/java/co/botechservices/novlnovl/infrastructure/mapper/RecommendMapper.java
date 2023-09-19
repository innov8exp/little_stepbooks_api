package co.botechservices.novlnovl.infrastructure.mapper;

import co.botechservices.novlnovl.domain.ads.dto.RecommendBookDto;
import co.botechservices.novlnovl.domain.ads.entity.RecommendEntity;
import co.botechservices.novlnovl.domain.book.entity.BookEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RecommendMapper extends BaseMapper<RecommendEntity> {

    List<BookEntity> findDefaultRecommendBooks();

    List<RecommendBookDto> listRecommendBooks();
}
