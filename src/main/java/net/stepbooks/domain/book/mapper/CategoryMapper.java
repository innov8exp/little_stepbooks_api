package net.stepbooks.domain.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.stepbooks.domain.dict.entity.CategoryEntity;

import java.util.List;

public interface CategoryMapper extends BaseMapper<CategoryEntity> {

    List<CategoryEntity> findCategoriesByBookId(String bookId);
}
