package net.stepbooks.domain.bookset.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.interfaces.admin.dto.BookSetFormDto;

public interface BookSetService extends IService<BookSet> {

    IPage<BookSet> findInPagingByCriteria(Page<BookSet> page, String name);

    void createBookSet(BookSetFormDto bookSet);

    void deleteBookSet(String id);

    BookSetFormDto getBookSet(String id);
}
