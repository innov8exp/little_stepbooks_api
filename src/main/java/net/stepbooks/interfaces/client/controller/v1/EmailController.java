package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import net.stepbooks.domain.email.service.EmailBusinessService;
import net.stepbooks.interfaces.client.dto.EmailDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/emails")
@SecurityRequirement(name = "Client Authentication")
public class EmailController {

    private final EmailBusinessService emailBusinessService;

    public EmailController(EmailBusinessService emailBusinessService) {
        this.emailBusinessService = emailBusinessService;
    }

    @PostMapping
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto emailDto) {
        emailBusinessService.sendSimpleMessage(emailDto);

        return ResponseEntity.ok().build();
    }
}
