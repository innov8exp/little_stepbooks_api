package co.botechservices.novlnovl.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("NOVL_BOOK_TAG_REF")
public class BookTagRefEntity {
    private String id;
    private String bookId;
    private String tagId;
}
