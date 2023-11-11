package net.stepbooks.domain.bookset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.bookset.entity.BookSetBook;
import net.stepbooks.domain.bookset.mapper.BookSetBookMapper;
import net.stepbooks.domain.bookset.service.BookSetBookService;
import org.springframework.stereotype.Service;

@Service
public class BookSetBookServiceImpl extends ServiceImpl<BookSetBookMapper, BookSetBook> implements BookSetBookService {
}
