package net.stepbooks.domain.advertisement.mapper;

import net.stepbooks.interfaces.client.dto.RecommendBookDto;
import net.stepbooks.domain.advertisement.entity.RecommendEntity;
import net.stepbooks.domain.book.entity.BookEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RecommendMapper extends BaseMapper<RecommendEntity> {

    List<BookEntity> findDefaultRecommendBooks();

    List<RecommendBookDto> listRecommendBooks();
}
