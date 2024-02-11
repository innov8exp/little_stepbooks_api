package net.stepbooks.domain.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_BOOK_CHAPTER_RECEIVE_HISTORY")
public class BookChapterReceiveHistory extends BaseEntity {
    private String userId;
    private String bookId;

    /**
     * '*' 表示领取本书全部课程，'10,11,12,13,14'表示领取ID为10~14的5个课程
     */
    private String chapters;
}
