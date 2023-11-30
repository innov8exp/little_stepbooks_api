package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("STEP_BOOK_MEDIA_REF")
public class BookMedia {

    private String id;
    private String bookId;
    private String mediaId;
    private String mediaUrl;

}
