package com.stepbook.domain.email.service;

import com.stepbook.interfaces.client.dto.EmailDto;
import com.stepbook.infrastructure.enums.EmailType;

public interface EmailService {

    void sendSimpleMessage(EmailDto emailDto);

    Boolean verifyValidationCode(String email, String code, EmailType emailType);
}

