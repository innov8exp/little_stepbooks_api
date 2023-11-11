package net.stepbooks.domain.bookset.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.entity.BookSetBook;
import net.stepbooks.domain.bookset.mapper.BookSetMapper;
import net.stepbooks.domain.bookset.service.BookSetBookService;
import net.stepbooks.domain.bookset.service.BookSetService;
import net.stepbooks.interfaces.admin.dto.BookSetFormDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookSetServiceImpl extends ServiceImpl<BookSetMapper, BookSet> implements BookSetService {

    private final BookSetBookService bookSetBookService;
    private final BookSetMapper bookSetMapper;

    @Override
    public IPage<BookSet> findInPagingByCriteria(Page<BookSet> page, String name) {
        return page(page, Wrappers.<BookSet>lambdaQuery());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createBookSet(BookSetFormDto bookSet) {
        BookSet newBookSet = BookSet.builder()
                .code(IdWorker.getIdStr())
                .name(bookSet.getName())
                .description(bookSet.getDescription())
                .build();
        save(newBookSet);
        List<BookSetBook> bookSetBooks = Arrays.stream(bookSet.getBookIds()).map(bookId -> BookSetBook.builder()
                .bookSetId(newBookSet.getId())
                .bookId(bookId)
                .build()).toList();
        bookSetBookService.saveBatch(bookSetBooks);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBookSet(String id) {
        bookSetBookService.remove(Wrappers.<BookSetBook>lambdaQuery().eq(BookSetBook::getBookSetId, id));
        bookSetMapper.deleteById(id);
    }

    @Override
    public BookSetFormDto getBookSet(String id) {
        BookSet bookSet = getById(id);
        List<BookSetBook> bookSetBooks = bookSetBookService
                .list(Wrappers.<BookSetBook>lambdaQuery().eq(BookSetBook::getBookSetId, id));
        BookSetFormDto bookSetFormDto = new BookSetFormDto();
        bookSetFormDto.setName(bookSet.getName());
        bookSetFormDto.setCode(bookSet.getCode());
        bookSetFormDto.setBookIds(bookSetBooks.stream().map(BookSetBook::getBookId).toArray(String[]::new));
        return bookSetFormDto;
    }
}
