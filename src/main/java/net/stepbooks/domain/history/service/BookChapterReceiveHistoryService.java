package net.stepbooks.domain.history.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.history.entity.BookChapterReceiveHistory;

public interface BookChapterReceiveHistoryService extends IService<BookChapterReceiveHistory> {

    void receive(String userId, String bookId, String chapters);

    IPage<BookChapterReceiveHistory> getPage(Page<BookChapterReceiveHistory> page, String userId, String bookId);

}

