package net.stepbooks.domain.bookshelf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;

import java.util.List;

public interface BookshelfAddLogService extends IService<BookshelfAddLog> {
    List<BookshelfAddLog> pagedFindByUserId(String userId);
}
