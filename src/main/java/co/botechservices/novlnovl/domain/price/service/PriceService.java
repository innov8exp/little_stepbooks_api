package co.botechservices.novlnovl.domain.price.service;

import co.botechservices.novlnovl.domain.price.entity.PriceEntity;

public interface PriceService {

    PriceEntity getBookChapterPrice(String bookId);
}
