package re.jmai.service;


import app.bean.Notification;
import app.bean.helloasso.HelloAssoPayment;
import app.bean.helloasso.HelloAssoPaymentStateEnum;
import app.bean.helloasso.notification.HelloAssoPaymentNotification;
import app.input.HelloAssoService;
import app.output.MailChimpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import re.jmai.bean.ProcessResult;
import re.jmai.bean.StatusPaymentEnum;
import re.jmai.entity.Configuration;
import re.jmai.entity.HelloAssoPaymentEntity;
import re.jmai.repository.ConfigurationRepository;
import re.jmai.repository.PaymentRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static re.jmai.service.ConfigurationService.MAIL_RECIPIENT;
import static re.jmai.service.ConfigurationService.PAYMENT_AUTOMATIC_ENABLED;


@Component
public class PaymentService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final long NUMBER_LATE_HOURS_ACCEPTED = 12l;

    private final ConfigurationRepository configurationRepository;
    private final PaymentRepository paymentRepository;
    private final MailService mailService;
    private final HelloAssoService helloAssoService;
    private final ConverterService converterService;
    private final MailChimpService mailChimpService;
    @Value("${mail.recipient}")
    private String mailRecipient;

    public PaymentService(ConfigurationRepository configurationRepository, PaymentRepository paymentRepository, MailService mailService, HelloAssoService helloAssoService, ConverterService converterService, MailChimpService mailChimpService) {
        this.configurationRepository = configurationRepository;
        this.paymentRepository = paymentRepository;
        this.mailService = mailService;
        this.helloAssoService = helloAssoService;
        this.converterService = converterService;
        this.mailChimpService = mailChimpService;
    }

    public void savePayments(List<HelloAssoPayment> payments) {
        for (HelloAssoPayment helloAssoPayment : payments) {
            paymentRepository.save(converterService.helloAssoToEntity(helloAssoPayment));
        }
    }

    public ProcessResult handleNewPayment(ProcessResult processResult, String helloAssoPaymentNotificationWrapper) throws IOException {

        if (helloAssoPaymentNotificationWrapper == null) {
            LOGGER.error("The payment is empty");
            processResult.getErrors().add("Le paiement est vide ");
            processResult.setStatusPayment(StatusPaymentEnum.fail);
            return processResult;
        }
        ObjectMapper mapper = new ObjectMapper();
        final HelloAssoPaymentNotification helloAssoPaymentNotification = mapper.readValue(helloAssoPaymentNotificationWrapper, HelloAssoPaymentNotification.class);

        // check if payment is real
        Map<Boolean, Notification> validPayment = helloAssoService.isValidPayment(helloAssoPaymentNotification);
        if (validPayment.containsKey(false)) {
            LOGGER.debug("The payment is not valid : {}", helloAssoPaymentNotificationWrapper);
            processResult.getErrors().add("Le paiement n'est pas valide :" + helloAssoPaymentNotificationWrapper);
            processResult.setStatusPayment(StatusPaymentEnum.fail);
            return processResult;
        }
        Notification notification = validPayment.get(true);
        final HelloAssoPaymentEntity paymentInDatabase = paymentRepository.findById(notification.getId()).get();

        if (paymentInDatabase == null) {
            // register payment in database (convert cent to euro)
            HelloAssoPaymentEntity payment = new HelloAssoPaymentEntity(notification.getId(), notification.getDate(), (float) notification.getAmount() / 100,
                    notification.getFirstName(), notification.getName(), notification.getEmail());
            HelloAssoPaymentEntity paymentSaved = paymentRepository.save(payment);

            if ("true".equals(configurationRepository.findById(PAYMENT_AUTOMATIC_ENABLED).orElse(new Configuration(PAYMENT_AUTOMATIC_ENABLED, "false")).getValue())) {
                // Check date
                LocalDateTime paymentDate = null;
                try {
                    paymentDate = LocalDateTime.parse(payment.getDate(), DateTimeFormatter.ISO_DATE_TIME);

                } catch (Exception e) {
                    LOGGER.error("Error parsing date in {}", payment);
                }
                if (paymentDate != null && LocalDateTime.now().isAfter(paymentDate.plusHours(NUMBER_LATE_HOURS_ACCEPTED))) {
                    final Configuration mailRecipientConfiguration = configurationRepository.findById(MAIL_RECIPIENT)
                            .orElse(new Configuration(MAIL_RECIPIENT, mailRecipient));

                    mailService.sendEmail(mailRecipientConfiguration.getValue(), "[Hellos] Paiement en retard",
                            "Un paiement a été reçu en retard.\nId : " + payment.getId());
                    processResult.setStatusPayment(StatusPaymentEnum.tooLate);
                    return processResult;
                }
                if (HelloAssoPaymentStateEnum.Waiting.name().equals(notification.getState())) {
                    final Configuration mailRecipientConfiguration = configurationRepository.findById(MAIL_RECIPIENT)
                            .orElse(new Configuration(MAIL_RECIPIENT, mailRecipient));

                    mailService.sendEmail(mailRecipientConfiguration.getValue(), "[Hellos] Paiement en attente",
                            "Un paiement a été reçu avec l'état 'Attente'.\nId : " + payment.getId());
                    processResult.setStatusPayment(StatusPaymentEnum.waiting);
                    return processResult;
                }

                LOGGER.info("automatic payment to be proceed");
                processResult = creditCyclosAccount(processResult, paymentSaved);
                final Configuration mailRecipientConfiguration = configurationRepository.findById(MAIL_RECIPIENT)
                        .orElse(new Configuration(MAIL_RECIPIENT, mailRecipient));
                if (StatusPaymentEnum.success.equals(processResult.getStatusPayment())) {
                    mailService.sendEmail(mailRecipientConfiguration.getValue(), "[Hellos] Paiement réussi :)",
                            "Un paiement a été effectué avec succès.\nId : " + paymentSaved.getId());
                    processResult.setStatusPayment(StatusPaymentEnum.successAuto);
                } else {
                    mailService.sendEmail(mailRecipientConfiguration.getValue(), "[Hellos] Paiement en échec :(",
                            "Un paiement n'a pas pu être effectué.\nId : " + paymentSaved.getId());
                }
            }
        } else {
            LOGGER.debug("Payment already inserted in database : {}", notification.getId());
        }

        return processResult;
    }

    public ProcessResult creditAccount(ProcessResult processResult, String paymentId) {
        Optional<HelloAssoPaymentEntity> payment = paymentRepository.findById(paymentId);
        if (!payment.isPresent()) {
            LOGGER.error("Payment not found : {}", paymentId);
            return processResult;
        }
        try {
            mailChimpService.addOneMember(converterService.helloAssoToSingleMailChimpMember(payment.get()));
            processResult.setStatusPayment(StatusPaymentEnum.success);
        }catch (Exception e){
            LOGGER.error("Erreur pendant l'ajout MailChimp : {}",e.getMessage());
            processResult.getErrors().add(e.getMessage());
            sendErrorEmail(processResult,paymentId);
        }
        return processResult;
    }

    private ProcessResult creditCyclosAccount(ProcessResult processResult, HelloAssoPaymentEntity payment) {
        /*processResult = cyclosService.creditAccount(processResult, payment.getId());
        payment.setStatus(processResult.getStatusPayment());
        if (!processResult.getErrors().isEmpty() && processResult.getErrors().toString().length() > Payment.ERROR_LENGTH) {
            payment.setError(processResult.getErrors().toString().substring(0, Payment.ERROR_LENGTH - 100));
        } else {
            payment.setError(processResult.getErrors().toString());
        }
        paymentRepository.save(payment);
        sendErrorEmail(processResult, payment.getId());*/
        return processResult;
    }

    public void sendErrorEmail(ProcessResult processResult, String paymentId) {
        if (!StatusPaymentEnum.success.equals(processResult.getStatusPayment())) {
            String body = "Liste des erreurs pour le paiement " + paymentId + ": \n " + processResult.getErrors().toString();
            String ERROR_SUBJECT = "[Hellos] Erreur lors du traitement";
            mailService.sendEmail(configurationRepository.findById(MAIL_RECIPIENT).get().getValue(), ERROR_SUBJECT, body);
        }
    }

    @Transactional
    public void creditAll() {
        final List<Optional<HelloAssoPaymentEntity>> paymentsToDo = paymentRepository.getByStatus(StatusPaymentEnum.todo);
        for (Optional<HelloAssoPaymentEntity> payment : paymentsToDo) {
            creditCyclosAccount(new ProcessResult(), payment.get());
        }
    }
}
