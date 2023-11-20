package net.stepbooks.domain.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.stepbooks.domain.book.entity.BookClassification;
import net.stepbooks.domain.book.mapper.BookClassificationMapper;
import net.stepbooks.domain.book.service.BookClassificationService;
import org.springframework.stereotype.Service;

@Service
public class BookClassificationServiceImpl extends ServiceImpl<BookClassificationMapper, BookClassification>
                                            implements BookClassificationService {
}
