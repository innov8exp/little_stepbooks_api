package net.stepbooks.domain.exercise.entity;

import lombok.Data;

@Data
public class ReadingContent {
    private Integer id;
    private String imgId;
    private String imgUrl;
    private String audioId;
    private String audioUrl;
    private String text;
}

