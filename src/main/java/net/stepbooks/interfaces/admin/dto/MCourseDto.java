package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.courses.course.enums.CourseNature;
import net.stepbooks.infrastructure.model.BaseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class MCourseDto extends BaseDto {
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
    private String videoKey;
    private CourseNature courseNature;
}
