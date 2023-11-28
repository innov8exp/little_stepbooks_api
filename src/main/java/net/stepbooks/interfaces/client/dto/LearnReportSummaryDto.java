package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class LearnReportSummaryDto {
    private String userId;
    private long totalDuration;
    private int totalDays;
}
