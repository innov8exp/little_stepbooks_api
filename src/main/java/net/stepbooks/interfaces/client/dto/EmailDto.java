package net.stepbooks.interfaces.client.dto;

import lombok.Data;
import net.stepbooks.infrastructure.enums.EmailType;

@Data
public class EmailDto {

    private String to;
    private String subject;
    private String text;
    private String code;
    private EmailType emailType;
}
