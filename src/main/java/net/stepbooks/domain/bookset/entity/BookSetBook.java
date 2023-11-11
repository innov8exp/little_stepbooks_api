package net.stepbooks.domain.bookset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("STEP_BOOK_SET_BOOK_REF")
public class BookSetBook {
    private String id;
    private String bookSetId;
    private String bookId;
}
