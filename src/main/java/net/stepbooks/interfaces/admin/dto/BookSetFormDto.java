package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

@Data
public class BookSetFormDto {
    private String code;
    private String name;
    private String description;
    private String[] bookIds;
}
