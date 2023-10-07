package com.stepbook.interfaces.client.controller.v1;

import com.stepbook.interfaces.client.dto.EmailDto;
import com.stepbook.domain.email.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto emailDto) {
        emailService.sendSimpleMessage(emailDto);

        return ResponseEntity.ok().build();
    }
}
