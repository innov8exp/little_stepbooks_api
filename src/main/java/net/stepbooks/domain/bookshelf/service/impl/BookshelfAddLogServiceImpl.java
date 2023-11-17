package net.stepbooks.domain.bookshelf.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.bookshelf.entity.BookshelfAddLog;
import net.stepbooks.domain.bookshelf.mapper.BookshelfAddLogMapper;
import net.stepbooks.domain.bookshelf.service.BookshelfAddLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookshelfAddLogServiceImpl extends ServiceImpl<BookshelfAddLogMapper, BookshelfAddLog>
        implements BookshelfAddLogService {
    @Override
    public List<BookshelfAddLog> pagedFindByUserId(String userId) {
        return list(Wrappers.<BookshelfAddLog>lambdaQuery().eq(BookshelfAddLog::getUserId, userId));
    }
}
