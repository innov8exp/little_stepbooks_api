package net.stepbooks.interfaces.client.controller.v1;

import lombok.RequiredArgsConstructor;
import net.stepbooks.domain.payment.service.PaymentService;
import net.stepbooks.infrastructure.util.ContextManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ContextManager contextManager;
    private final PaymentService paymentService;

}
