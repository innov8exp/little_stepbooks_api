package net.stepbooks.domain.history.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.book.entity.BookChapter;
import net.stepbooks.domain.book.service.BookChapterService;
import net.stepbooks.domain.history.entity.ReadingHistory;
import net.stepbooks.domain.history.mapper.ReadingHistoryMapper;
import net.stepbooks.domain.history.service.ReadingHistoryService;
import net.stepbooks.interfaces.client.dto.LearnReportDto;
import net.stepbooks.interfaces.client.dto.ReadHistoryForm;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingHistoryServiceImpl extends ServiceImpl<ReadingHistoryMapper, ReadingHistory>
        implements ReadingHistoryService {

    private final ReadingHistoryMapper readingHistoryMapper;
    private final BookChapterService bookChapterService;

    @Override
    public List<LearnReportDto> getUserTodayReports(String userId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return readingHistoryMapper.getUserReportsByDay(userId, today, today, yesterday);
    }

    @Override
    public List<LearnReportDto> getUserYesterdayReports(String userId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return readingHistoryMapper.getUserReportsByDay(userId, yesterday, today, yesterday);
    }

    @Override
    public List<LearnReportDto> getUserHistoryReports(String userId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String yesterday = LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return readingHistoryMapper.getUserReportsByDay(userId, null, today, yesterday);
    }

    @Override
    public void createReadingHistory(String userId, String bookId, ReadHistoryForm form) {
        BookChapter bookChapter = bookChapterService.getById(form.getChapterId());
        ReadingHistory readingHistory = getOne(Wrappers.<ReadingHistory>lambdaQuery().eq(ReadingHistory::getUserId, userId)
                .eq(ReadingHistory::getBookId, bookId));
        if (ObjectUtils.isEmpty(readingHistory)) {
            readingHistory = new ReadingHistory();
            readingHistory.setUserId(userId);
            readingHistory.setBookId(bookId);
            readingHistory.setMaxChapterNo(bookChapter.getChapterNo());
            save(readingHistory);
            return;
        }
        if (bookChapter.getChapterNo() > readingHistory.getMaxChapterNo()) {
            readingHistory.setMaxChapterNo(bookChapter.getChapterNo());
        }
        updateById(readingHistory);
    }
}
