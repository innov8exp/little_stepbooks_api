package net.stepbooks.domain.bookshelf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;
import net.stepbooks.interfaces.client.dto.BookshelfAddLogDto;

public interface BookshelfAddLogService extends IService<BookshelfAddLog> {
    IPage<BookshelfAddLogDto> pagedFindByUserId(String userId, Page<BookshelfAddLogDto> page, String keyword);
}
