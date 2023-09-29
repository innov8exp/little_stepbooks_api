package com.stepbook.infrastructure.mapper;

import com.stepbook.domain.book.entity.BookEntity;
import com.stepbook.domain.library.entity.FavoriteEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper extends BaseMapper<FavoriteEntity> {

    List<BookEntity> findBooksFromFavoriteByUserId(@Param("userId") String userId);
}
