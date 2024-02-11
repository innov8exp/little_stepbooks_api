package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.BookChapterReceiveHistory;
import net.stepbooks.domain.history.mapper.BookChapterReceiveHistoryMapper;
import net.stepbooks.domain.history.service.BookChapterReceiveHistoryService;
import net.stepbooks.infrastructure.util.CommonUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookChapterReceiveHistoryServiceImpl extends ServiceImpl<BookChapterReceiveHistoryMapper, BookChapterReceiveHistory>
        implements BookChapterReceiveHistoryService {

    private final BookChapterReceiveHistoryMapper bookChapterReceiveHistoryMapper;

    @Override
    public void receive(String userId, String bookId, String chapters) {
        BookChapterReceiveHistory bookChapterReceiveHistory = bookChapterReceiveHistoryMapper
                .selectOne(Wrappers.<BookChapterReceiveHistory>lambdaQuery()
                        .eq(BookChapterReceiveHistory::getUserId, userId)
                        .eq(BookChapterReceiveHistory::getBookId, bookId));
        if (bookChapterReceiveHistory == null) {
            bookChapterReceiveHistory = new BookChapterReceiveHistory();
            bookChapterReceiveHistory.setBookId(bookId);
            bookChapterReceiveHistory.setUserId(userId);
            bookChapterReceiveHistory.setChapters(chapters);
            save(bookChapterReceiveHistory);
        } else {
            String oldChapters = bookChapterReceiveHistory.getChapters();
            String newChapters = CommonUtil.
                    combineCommaString(oldChapters, chapters);

            if (!oldChapters.equals(newChapters)) {
                bookChapterReceiveHistory.setChapters(newChapters);
                updateById(bookChapterReceiveHistory);
            }

        }
    }

    @Override
    public IPage<BookChapterReceiveHistory> getPage(Page<BookChapterReceiveHistory> page, String userId, String bookId) {
        LambdaQueryWrapper<BookChapterReceiveHistory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(userId), BookChapterReceiveHistory::getUserId, userId);
        wrapper.eq(ObjectUtils.isNotEmpty(bookId), BookChapterReceiveHistory::getBookId, bookId);
        return this.baseMapper.selectPage(page, wrapper);
    }
}
