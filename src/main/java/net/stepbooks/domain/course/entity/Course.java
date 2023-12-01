package net.stepbooks.domain.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import net.stepbooks.domain.course.enums.CourseNature;
import net.stepbooks.infrastructure.model.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("STEP_COURSE")
public class Course extends BaseEntity {
    private String bookId;
    private String name;
    private String description;
    private String author;
    private String authorIntroduction;
    private String duration;
    private String coverImgId;
    private String coverImgUrl;
    private String videoId;
    private String videoUrl;
    private CourseNature courseNature;
}
