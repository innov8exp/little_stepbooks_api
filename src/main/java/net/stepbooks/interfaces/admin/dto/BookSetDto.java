package net.stepbooks.interfaces.admin.dto;

import lombok.Data;

@Data
public class BookSetDto {
    private String id;
    private String code;
    private String name;
    private String description;
    private String mnpQRCode;
}
