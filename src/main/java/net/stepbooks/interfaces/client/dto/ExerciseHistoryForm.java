package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ExerciseHistoryForm {

    @NonNull
    private String exerciseId;

    @NonNull
    private String courseId;

    @NonNull
    private Integer score;
}
