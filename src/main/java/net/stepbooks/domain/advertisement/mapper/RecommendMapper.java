package net.stepbooks.domain.advertisement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.advertisement.entity.RecommendEntity;
import net.stepbooks.domain.book.entity.BookEntity;
import net.stepbooks.interfaces.client.dto.RecommendBookDto;

import java.util.List;

public interface RecommendMapper extends BaseMapper<RecommendEntity> {

    List<BookEntity> findDefaultRecommendBooks();

    List<RecommendBookDto> listRecommendBooks();
}
