package net.stepbooks.interfaces.client.dto;

import lombok.Data;

@Data
public class ReadHistoryForm {
    private String chapterId;
    private int chapterNo;
}
