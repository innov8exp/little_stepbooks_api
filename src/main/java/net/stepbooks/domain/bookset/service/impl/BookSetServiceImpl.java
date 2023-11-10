package net.stepbooks.domain.bookset.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.mapper.BookSetMapper;
import net.stepbooks.domain.bookset.service.BookSetService;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookSetServiceImpl extends ServiceImpl<BookSetMapper, BookSet> implements BookSetService {

    private final BookSetMapper bookSetMapper;

    @Override
    public IPage<BookSet> findInPagingByCriteria(Page<BookSet> page, String name) {
        return page(page, Wrappers.<BookSet>lambdaQuery().likeLeft(BookSet::getName, name));
    }
}
