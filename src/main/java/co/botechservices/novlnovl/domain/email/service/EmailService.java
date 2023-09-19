package co.botechservices.novlnovl.domain.email.service;

import co.botechservices.novlnovl.domain.email.dto.EmailDto;
import co.botechservices.novlnovl.infrastructure.enums.EmailType;

public interface EmailService {

    void sendSimpleMessage(EmailDto emailDto);

    Boolean verifyValidationCode(String email, String code, EmailType emailType);
}

