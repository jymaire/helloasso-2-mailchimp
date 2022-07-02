package re.jmai.controller;

import app.bean.helloasso.HelloAssoPayment;
import app.input.HelloAssoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import re.jmai.bean.AdminConfig;
import re.jmai.repository.PaymentRepository;
import re.jmai.service.ConfigurationService;
import re.jmai.service.PaymentService;

import java.util.List;

import static re.jmai.service.ConfigurationService.MAIL_RECIPIENT;
import static re.jmai.service.ConfigurationService.PAYMENT_AUTOMATIC_ENABLED;

@Controller
public class AdminController {
    private final ConfigurationService configurationService;
    // à voir si on change le hello asso service pour le remplacer par celui du module core
    private final HelloAssoService helloAssoService;
    private final PaymentService paymentService;

    @Value("${hello-asso.fetch.nb-days}")
    private int nbDaysToFetch;

    public AdminController(ConfigurationService configurationService, HelloAssoService helloAssoService, PaymentService paymentService) {
        this.configurationService = configurationService;
        this.helloAssoService = helloAssoService;
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public AdminConfig configuration(Model model) {
        var all = configurationService.findAll();
        AdminConfig adminConfig = new AdminConfig(Boolean.parseBoolean(all.get(PAYMENT_AUTOMATIC_ENABLED)), all.get(MAIL_RECIPIENT));
        model.addAttribute("adminConfig", adminConfig);
        return adminConfig;
    }

    @Transactional
    @RequestMapping(value = "/admin", method = RequestMethod.POST)
    public String save(Model model, @ModelAttribute AdminConfig adminConfig) {
        configurationService.save(Boolean.valueOf(adminConfig.isPaymentAutomaticEnabled()).toString(), adminConfig.getMailRecipient());
        return "redirect:/admin";
    }

    @Transactional
    @RequestMapping(value = "/fetch", method = RequestMethod.GET)
    public String fetchDataFromHelloAsso(Model model) throws IllegalAccessException {
        final List<HelloAssoPayment> payments = helloAssoService.getPaymentsFor(nbDaysToFetch);
        paymentService.savePayments(payments);
        return "redirect:/list";
    }
}
