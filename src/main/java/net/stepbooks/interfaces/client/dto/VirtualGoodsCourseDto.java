package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.infrastructure.model.BaseDto;

@Data
public class VirtualGoodsCourseDto extends BaseDto {

    private String id;

    private Integer sortIndex;
    private String categoryId;
    private String goodsId;
    private String courseId;

    private CourseDto course;

}
