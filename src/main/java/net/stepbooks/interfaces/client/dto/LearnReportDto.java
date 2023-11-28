package net.stepbooks.interfaces.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LearnReportDto {
    private String bookId;
    private String bookName;
    private String bookDescription;
    private String bookImgId;
    private String bookImgUrl;
    private float bookProgress;
    private long learnDuration;
    private LocalDateTime lastLearnTime;
    private int maxChapterNo;
    private int totalChapterNo;
}
