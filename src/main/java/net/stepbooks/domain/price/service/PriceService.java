package net.stepbooks.domain.price.service;

import net.stepbooks.domain.price.entity.PriceEntity;

public interface PriceService {

    PriceEntity getBookChapterPrice(String bookId);
}
