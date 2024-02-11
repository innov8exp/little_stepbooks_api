package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.history.entity.BookCourseReceiveHistory;
import net.stepbooks.domain.history.mapper.BookCourseReceiveHistoryMapper;
import net.stepbooks.domain.history.service.BookCourseReceiveHistoryService;
import net.stepbooks.infrastructure.util.CommonUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookCourseReceiveHistoryServiceImpl extends ServiceImpl<BookCourseReceiveHistoryMapper, BookCourseReceiveHistory>
        implements BookCourseReceiveHistoryService {

    private final BookCourseReceiveHistoryMapper bookCourseReceiveHistoryMapper;

    @Override
    public void receive(String userId, String bookId, String courses) {
        BookCourseReceiveHistory bookCourseReceiveHistory = bookCourseReceiveHistoryMapper
                .selectOne(Wrappers.<BookCourseReceiveHistory>lambdaQuery()
                        .eq(BookCourseReceiveHistory::getUserId, userId)
                        .eq(BookCourseReceiveHistory::getBookId, bookId));
        if (bookCourseReceiveHistory == null) {
            bookCourseReceiveHistory = new BookCourseReceiveHistory();
            bookCourseReceiveHistory.setBookId(bookId);
            bookCourseReceiveHistory.setUserId(userId);
            bookCourseReceiveHistory.setCourses(courses);
            save(bookCourseReceiveHistory);
        } else {
            String oldCourses = bookCourseReceiveHistory.getCourses();
            String newCourses = CommonUtil.
                    combineCommaString(oldCourses, courses);

            if (!oldCourses.equals(newCourses)) {
                bookCourseReceiveHistory.setCourses(newCourses);
                updateById(bookCourseReceiveHistory);
            }

        }
    }

    @Override
    public IPage<BookCourseReceiveHistory> getPage(Page<BookCourseReceiveHistory> page, String userId, String bookId) {
        LambdaQueryWrapper<BookCourseReceiveHistory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtils.isNotEmpty(userId), BookCourseReceiveHistory::getUserId, userId);
        wrapper.eq(ObjectUtils.isNotEmpty(bookId), BookCourseReceiveHistory::getBookId, bookId);
        return this.baseMapper.selectPage(page, wrapper);
    }
}
