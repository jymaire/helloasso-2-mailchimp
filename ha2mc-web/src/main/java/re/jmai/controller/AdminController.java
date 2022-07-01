package re.jmai.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import re.jmai.bean.AdminConfig;
import re.jmai.service.ConfigurationService;
import re.jmai.service.HelloAssoService;

import static re.jmai.service.ConfigurationService.*;

@Controller
public class AdminController {
    private final ConfigurationService configurationService;
    // à voir si on change le hello asso service pour le remplacer par celui du module core
    private final HelloAssoService helloAssoService;

    @Value("${hello-asso.fetch.nb-days}")
    private int nbDaysToFetch;

    public AdminController(ConfigurationService configurationService, HelloAssoService helloAssoService) {
        this.configurationService = configurationService;
        this.helloAssoService = helloAssoService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public AdminConfig configuration(Model model) {
        var all = configurationService.findAll();
        AdminConfig adminConfig = new AdminConfig(Boolean.parseBoolean(all.get(PAYMENT_CYCLOS_ENABLED)), Boolean.parseBoolean(all.get(PAYMENT_AUTOMATIC_ENABLED)), all.get(MAIL_RECIPIENT));
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
        //helloAssoService.getPaymentsFor(nbDaysToFetch);
        return "redirect:/list";
    }
}
