package net.stepbooks.domain.email.service;

import net.stepbooks.infrastructure.enums.EmailType;
import net.stepbooks.application.dto.client.EmailDto;

public interface EmailService {

    void sendSimpleMessage(EmailDto emailDto);

    Boolean verifyValidationCode(String email, String code, EmailType emailType);
}

