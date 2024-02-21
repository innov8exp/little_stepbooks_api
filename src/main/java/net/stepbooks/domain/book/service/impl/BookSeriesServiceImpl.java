package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.domain.book.entity.BookSeries;
import net.stepbooks.domain.book.mapper.BookSeriesMapper;
import net.stepbooks.domain.book.service.BookSeriesService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookSeriesServiceImpl extends ServiceImpl<BookSeriesMapper, BookSeries> implements BookSeriesService {
}
