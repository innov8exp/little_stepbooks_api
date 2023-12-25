package net.stepbooks.domain.book.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_BOOK_QR_CODE")
public class BookQRCode extends BaseEntity {

    private String bookId;
    private String qrCode;
}
