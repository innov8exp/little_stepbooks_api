package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.mapper.BookChapterMapper;
import net.stepbooks.domain.book.service.BookChapterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookChapterServiceImpl extends ServiceImpl<BookChapterMapper, BookChapter> implements BookChapterService {

    private final BookChapterMapper bookChapterMapper;

    @Override
    public List<BookChapter> getBookChapters(String bookId) {
        return bookChapterMapper.selectList(Wrappers.<BookChapter>lambdaQuery()
                .eq(BookChapter::getBookId, bookId)
                .orderBy(true, true, BookChapter::getChapterNo));
    }

    @Override
    public Long getMaxChapterNo(String bookId) {
        return bookChapterMapper.getMaxChapterNoByBookId(bookId);
    }
}
