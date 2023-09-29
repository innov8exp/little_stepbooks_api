package com.stepbook.domain.email.dto;

import com.stepbook.infrastructure.enums.EmailType;
import lombok.Data;

@Data
public class EmailDto {

    private String to;
    private String subject;
    private String text;
    private String code;
    private EmailType emailType;
}
