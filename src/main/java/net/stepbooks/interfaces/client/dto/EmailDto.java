package net.stepbooks.interfaces.client.dto;

import net.stepbooks.infrastructure.enums.EmailType;
import lombok.Data;

@Data
public class EmailDto {

    private String to;
    private String subject;
    private String text;
    private String code;
    private EmailType emailType;
}
