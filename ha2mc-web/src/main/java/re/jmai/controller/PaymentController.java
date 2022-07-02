package re.jmai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import re.jmai.bean.ProcessResult;
import re.jmai.entity.HelloAssoPaymentEntity;
import re.jmai.repository.PaymentRepository;
import re.jmai.service.PaymentService;

import java.util.List;

@Controller
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public PaymentController(PaymentRepository paymentRepository, PaymentService paymentService) {
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String payments(Model model) {
        List<HelloAssoPaymentEntity> all = paymentRepository.findAll();
        model.addAttribute("payments", all);
        return "list";
    }

    @Transactional
    @RequestMapping(value = "/list", method = RequestMethod.POST, params = "delete")
    public String delete(@RequestParam(name = "id") String id, Model model) {
        paymentRepository.deleteById(id);
        return "redirect:/list";
    }

    @Transactional
    @RequestMapping(value = "/list", method = RequestMethod.POST, params = "credit")
    public String credit(@RequestParam(name = "id") String id, Model model) {
        ProcessResult processResult = new ProcessResult();
        paymentService.creditAccount(processResult, id);
        return "redirect:/list";
    }
}
