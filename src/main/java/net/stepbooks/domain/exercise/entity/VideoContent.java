package net.stepbooks.domain.exercise.entity;

import lombok.Data;

@Data
public class VideoContent {
    private Integer id;
    private String imgId;
    private String imgUrl;
    private String videoId;
    private String videoUrl;
}
