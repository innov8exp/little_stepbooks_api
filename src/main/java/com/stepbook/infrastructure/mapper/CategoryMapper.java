package com.stepbook.infrastructure.mapper;

import com.stepbook.domain.dict.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface CategoryMapper extends BaseMapper<CategoryEntity> {

    List<CategoryEntity> findCategoriesByBookId(String bookId);
}
