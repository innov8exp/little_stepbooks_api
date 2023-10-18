package net.stepbooks.domain.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_COURSE_CONTENT")
public class CourseContentEntity extends BaseEntity {

    private String courseId;
    private Integer chapterNumber;
    private String chapterName;
    private String chapterContentLink;
    private String chapterDuration;
    private String chapterContentDescription;
}
