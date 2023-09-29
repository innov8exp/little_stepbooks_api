package com.stepbook.domain.book.dto;

import com.stepbook.infrastructure.enums.ChargeType;
import com.stepbook.infrastructure.model.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookDetailDto extends BaseDto {
    private String bookName;
    private String author;
    private String coverImg;
    private String introduction;
    private String[] keywords;
    private Boolean isSerialized;
    private Boolean hasEnding;
    private String status;
    private String[] categories;

    private ChargeType chargeType;
    private BigDecimal price;

    private Long chapterCount;

    private Double rating;
    private Long ratingCount;
    private Long favoritesCount;
    private Long readCount;
    private Long commentCount;

    private Boolean inBookshelf;
    private Boolean inFavorite;

    private Integer rateTime;
}
