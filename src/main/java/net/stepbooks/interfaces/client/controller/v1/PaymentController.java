package net.stepbooks.interfaces.client.controller.v1;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Client Authentication")
public class PaymentController {

    private final ContextManager contextManager;
    private final PaymentService paymentService;

}
