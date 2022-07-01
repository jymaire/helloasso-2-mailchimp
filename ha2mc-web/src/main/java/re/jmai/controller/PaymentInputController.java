package re.jmai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import re.jmai.bean.ProcessResult;
import re.jmai.service.PaymentService;

import java.io.IOException;

@RestController
public class PaymentInputController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final PaymentService paymentService;

    public PaymentInputController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping("/helloasso/payment")
    public void receivePayment(@RequestBody String payment) throws IOException {
        // INFO level the time to set the systm up
        LOGGER.debug("Received payment : {}", payment);
        ProcessResult processResult = new ProcessResult();
        paymentService.handleNewPayment(processResult, payment);
    }
}