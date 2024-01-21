package net.stepbooks.interfaces.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseDto {
    private String courseId;
    private String name;
    private String nature;
    private Integer sortNo;
    private String coverImgId;
    private String coverImgUrl;
    private String json;
    private String answerJson;
}
