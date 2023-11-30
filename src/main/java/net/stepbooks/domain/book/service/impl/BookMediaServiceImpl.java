package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.book.entity.BookMedia;
import net.stepbooks.domain.book.mapper.BookMediaMapper;
import net.stepbooks.domain.book.service.BookMediaService;
import org.springframework.stereotype.Service;

@Service
public class BookMediaServiceImpl extends ServiceImpl<BookMediaMapper, BookMedia> implements BookMediaService {
}
