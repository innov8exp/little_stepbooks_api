package co.botechservices.novlnovl.domain.email.dto;

import co.botechservices.novlnovl.infrastructure.enums.EmailType;
import lombok.Data;

@Data
public class EmailDto {

    private String to;
    private String subject;
    private String text;
    private String code;
    private EmailType emailType;
}
