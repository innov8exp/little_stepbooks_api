package net.stepbooks.domain.history.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("STEP_BOOK_COURSE_RECEIVE_HISTORY")
public class BookCourseReceiveHistory extends BaseEntity {
    private String userId;
    private String bookId;

    /**
     * '*' 表示领取本书全部章节，'6,7,8,9,10'表示领取ID为6~10的5个章节（有声书）
     */
    private String courses;
}
