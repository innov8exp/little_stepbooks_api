package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookSeries;
import net.stepbooks.domain.book.service.BookSeriesService;
import net.stepbooks.domain.book.service.BookService;
import net.stepbooks.domain.history.entity.BookCourseReceiveHistory;
import net.stepbooks.domain.history.mapper.BookCourseReceiveHistoryMapper;
import net.stepbooks.domain.history.service.BookCourseReceiveHistoryService;
import net.stepbooks.infrastructure.AppConstants;
import net.stepbooks.infrastructure.util.CommonUtil;
import net.stepbooks.interfaces.admin.dto.BookDto;
import net.stepbooks.interfaces.admin.dto.BookReceiveSummaryDto;
import net.stepbooks.interfaces.admin.dto.BookSeriesDto;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookCourseReceiveHistoryServiceImpl extends ServiceImpl<BookCourseReceiveHistoryMapper, BookCourseReceiveHistory>
        implements BookCourseReceiveHistoryService {

    private final BookCourseReceiveHistoryMapper bookCourseReceiveHistoryMapper;

    private final BookService bookService;

    private final BookSeriesService bookSeriesService;

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

            if (oldCourses == null || !oldCourses.equals(newCourses)) {
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

    @Override
    public BookReceiveSummaryDto receiveSummary(String userId) {
        Page<BookCourseReceiveHistory> page = Page.of(1, AppConstants.MAX_PAGE_SIZE);
        IPage<BookCourseReceiveHistory> pages = getPage(page, userId, null);
        BookReceiveSummaryDto summaryDto = new BookReceiveSummaryDto();

        List<BookSeriesDto> series = new ArrayList<>();
        List<BookDto> books = new ArrayList<>();

        summaryDto.setSeries(series);
        summaryDto.setBooks(books);

        for (BookCourseReceiveHistory receiveHistory : pages.getRecords()) {
            String bookId = receiveHistory.getBookId();
            BookDto book = bookService.findBookById(bookId);
            LocalDateTime receiveAt = receiveHistory.getCreatedAt();
            if (book.getSeriesId() != null) {
                BookSeriesDto seriesDto = null;
                for (BookSeriesDto tmpSeriesDto : series) {
                    if (tmpSeriesDto.getId().equals(book.getSeriesId())) {
                        seriesDto = tmpSeriesDto;
                    }
                }
                if (seriesDto == null) {
                    BookSeries bookSeries = bookSeriesService.getById(book.getSeriesId());
                    seriesDto = new BookSeriesDto();
                    BeanUtils.copyProperties(bookSeries, seriesDto);
                    seriesDto.setReceiveAt(receiveAt);
                    series.add(seriesDto);
                }

                if (seriesDto.getBooks() == null) {
                    seriesDto.setBooks(new ArrayList<>());
                }
                seriesDto.getBooks().add(book);
            } else {
                books.add(book);
            }
        }
        return summaryDto;
    }
}
