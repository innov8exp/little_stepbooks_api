package net.stepbooks.domain.email.service;

import net.stepbooks.interfaces.client.dto.EmailDto;
import net.stepbooks.infrastructure.enums.EmailType;

public interface EmailService {

    void sendSimpleMessage(EmailDto emailDto);

    Boolean verifyValidationCode(String email, String code, EmailType emailType);
}

