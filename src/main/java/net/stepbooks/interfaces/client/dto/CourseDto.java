package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.stepbooks.domain.course.enums.CourseNature;
import net.stepbooks.infrastructure.model.BaseDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseDto extends BaseDto {
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
    private String videoObjectKey;
}
