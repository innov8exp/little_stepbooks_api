package net.stepbooks.domain.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_COURSE")
public class CourseEntity extends BaseEntity {

    private String bookId;
    private String courseName;
    private String courseImgLink;
    private String courseDescription;
    private String courseAuthor;
}
