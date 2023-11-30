package net.stepbooks.domain.bookshelf.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;
import net.stepbooks.domain.bookshelf.mapper.BookshelfAddLogMapper;
import net.stepbooks.domain.bookshelf.service.BookshelfAddLogService;
import net.stepbooks.interfaces.client.dto.BookshelfAddLogDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookshelfAddLogServiceImpl extends ServiceImpl<BookshelfAddLogMapper, BookshelfAddLog>
        implements BookshelfAddLogService {

    private final BookshelfAddLogMapper bookshelfAddLogMapper;

    @Override
    public IPage<BookshelfAddLogDto> pagedFindByUserId(String userId, Page<BookshelfAddLogDto> page, String keyword) {
        return bookshelfAddLogMapper.selectBookshelfAddLogByUserId(page, userId, keyword);
    }
}
