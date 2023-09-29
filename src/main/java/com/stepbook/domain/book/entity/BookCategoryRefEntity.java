package com.stepbook.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_BOOK_CATEGORY_REF")
public class BookCategoryRefEntity {
    private String id;
    private String bookId;
    private String categoryId;
}
