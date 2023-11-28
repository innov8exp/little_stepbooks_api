package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class LearnHistoryForm {
    private String bookId;
    private Long duration;
}
