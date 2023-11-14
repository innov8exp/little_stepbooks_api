package net.stepbooks.domain.bookset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.stepbooks.domain.book.entity.Book;
import net.stepbooks.domain.bookset.entity.BookSet;

import java.util.List;

public interface BookSetMapper extends BaseMapper<BookSet> {
    List<Book> findBooksByBookSetId(String bookSetId);

    IPage<BookSet> findInPagingByCriteria(Page<BookSet> page, String name);
}
