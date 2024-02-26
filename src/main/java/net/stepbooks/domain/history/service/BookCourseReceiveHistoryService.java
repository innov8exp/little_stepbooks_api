package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.history.entity.BookCourseReceiveHistory;
import net.stepbooks.interfaces.admin.dto.BookReceiveSummaryDto;

public interface BookCourseReceiveHistoryService extends IService<BookCourseReceiveHistory> {

    void receive(String userId, String bookId, String courses);

    IPage<BookCourseReceiveHistory> getPage(Page<BookCourseReceiveHistory> page, String userId, String bookId);

    BookReceiveSummaryDto receiveSummary(String userId);
}
