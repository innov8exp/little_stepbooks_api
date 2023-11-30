package net.stepbooks.domain.bookshelf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;
import net.stepbooks.interfaces.client.dto.BookshelfAddLogDto;

public interface BookshelfAddLogMapper extends BaseMapper<BookshelfAddLog> {
    IPage<BookshelfAddLogDto> selectBookshelfAddLogByUserId(Page<BookshelfAddLogDto> page, String userId, String keyword);
}
