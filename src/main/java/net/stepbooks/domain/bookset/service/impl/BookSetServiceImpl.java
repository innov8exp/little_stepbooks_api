package net.stepbooks.domain.bookset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.bookset.entity.BookSet;
import net.stepbooks.domain.bookset.mapper.BookSetMapper;
import net.stepbooks.domain.bookset.service.BookSetService;
import org.springframework.stereotype.Service;

@Service
public class BookSetServiceImpl extends ServiceImpl<BookSetMapper, BookSet> implements BookSetService {
}
