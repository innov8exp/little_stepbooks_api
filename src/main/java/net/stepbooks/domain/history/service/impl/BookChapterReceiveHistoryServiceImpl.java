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
import net.stepbooks.domain.history.entity.BookChapterReceiveHistory;
import net.stepbooks.domain.history.mapper.BookChapterReceiveHistoryMapper;
import net.stepbooks.domain.history.service.BookChapterReceiveHistoryService;
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
public class BookChapterReceiveHistoryServiceImpl extends ServiceImpl<BookChapterReceiveHistoryMapper, BookChapterReceiveHistory>
        implements BookChapterReceiveHistoryService {

    private final BookChapterReceiveHistoryMapper bookChapterReceiveHistoryMapper;

    private final BookService bookService;

    private final BookSeriesService bookSeriesService;

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

            if (oldChapters == null || !oldChapters.equals(newChapters)) {
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

    @Override
    public BookReceiveSummaryDto receiveSummary(String userId) {
        Page<BookChapterReceiveHistory> page = Page.of(1, AppConstants.MAX_PAGE_SIZE);
        IPage<BookChapterReceiveHistory> pages = getPage(page, userId, null);
        BookReceiveSummaryDto summaryDto = new BookReceiveSummaryDto();

        List<BookSeriesDto> series = new ArrayList<>();
        List<BookDto> books = new ArrayList<>();

        summaryDto.setSeries(series);
        summaryDto.setBooks(books);

        for (BookChapterReceiveHistory receiveHistory : pages.getRecords()) {
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

                //按seriesNo从小到大排序
                boolean isAdded = false;
                for (int i = 0; i < seriesDto.getBooks().size(); i++) {
                    BookDto tmpBook = seriesDto.getBooks().get(i);
                    if (tmpBook.getSeriesNo() > book.getSeriesNo()) {

                        seriesDto.getBooks().add(i, book);
                        isAdded = true;
                        break;
                    }
                }

                if (!isAdded) {
                    seriesDto.getBooks().add(book);
                }

            } else {
                books.add(book);
            }
        }
        return summaryDto;
    }
}
