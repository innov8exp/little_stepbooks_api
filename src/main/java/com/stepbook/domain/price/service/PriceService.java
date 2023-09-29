package com.stepbook.domain.price.service;

import com.stepbook.domain.price.entity.PriceEntity;

public interface PriceService {

    PriceEntity getBookChapterPrice(String bookId);
}
