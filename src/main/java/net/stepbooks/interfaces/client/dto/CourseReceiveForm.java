package net.stepbooks.interfaces.client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseReceiveForm {
    @NotNull
    private String bookId;

    /**
     * '*' 表示领取本书全部课程，'10,11,12,13,14'表示领取ID为10~14的5个课程
     */
    @NotNull
    private String courses;
}
