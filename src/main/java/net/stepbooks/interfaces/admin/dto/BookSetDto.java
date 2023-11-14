package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookSetDto {
    private String id;
    private String code;
    private String name;
    private String description;
    private String mnpQRCode;
    private List<String> bookIds;
}
