package re.jmai.service;

import app.bean.Notification;
import app.bean.helloasso.HelloAssoPayment;
import app.bean.mailchimp.MailChimpMember;
import app.input.HelloAssoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import re.jmai.bean.StatusPaymentEnum;
import re.jmai.entity.HelloAssoPaymentEntity;

import java.util.HashMap;
import java.util.Map;

import static app.input.HelloAssoService.*;

@Service
public class ConverterService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final HelloAssoService helloAssoService;

    public ConverterService(HelloAssoService helloAssoService) {
        this.helloAssoService = helloAssoService;
    }

    public HelloAssoPaymentEntity helloAssoToEntity(HelloAssoPayment helloAssoPayment) {
        String token = null;
        try {
            token = helloAssoService.getHelloAssoAccessToken();
            final Map<String, String> extraFields = helloAssoService.getExtraFields(helloAssoPayment.getOrder().getId(), token);

            HelloAssoPaymentEntity payment = HelloAssoPaymentEntity.HelloAssoPaymentEntityBuilder.aHelloAssoPaymentEntity()
                    .withDate(helloAssoPayment.getDate())
                    .withEmail(helloAssoPayment.getPayer().getEmail())
                    .withPayerFirstName(helloAssoPayment.getPayer().getFirstName())
                    .withPayerLastName(helloAssoPayment.getPayer().getLastName())
                    .withId(helloAssoPayment.getId())
                    .withAmount((float) helloAssoPayment.getAmount() / (float) 100)
                    .withEntrepriseProjet(extraFields.get(ENTREPRISE))
                    .withTarif(extraFields.get(TARIF))
                    .withCodePostal(extraFields.get(CODE_POSTAL))
                    .withHelloAssoStatus(helloAssoPayment.getState().name())
                    .withStatus(StatusPaymentEnum.todo)
                    .build();
            return payment;
        } catch (IllegalAccessException illegalAccessException) {
            LOGGER.error(illegalAccessException.getMessage());
        } finally {
            if (token != null) helloAssoService.disconnect(token);
        }
        return null;
    }

    public HelloAssoPaymentEntity notificationToHelloAssoEntity(Notification notification) {
        // TODO : voir ce qu'on a dans la notif pour extraire l'id de l'order
        HelloAssoPaymentEntity helloAssoToEntity = new HelloAssoPaymentEntity(notification.getId(), notification.getDate(), (float) notification.getAmount() / 100,
                notification.getFirstName(), notification.getName(), notification.getEmail());
        return helloAssoToEntity;
    }

    public MailChimpMember helloAssoToSingleMailChimpMember(HelloAssoPaymentEntity helloAssoPayment) {

        MailChimpMember mailChimpMember = new MailChimpMember();
        mailChimpMember.setEmail_address(helloAssoPayment.getEmail().stripTrailing());
        HashMap<String, Object> mergeFields = new HashMap<>();
        mergeFields.put("PAYEMENT", "Carte bancaire");
        mailChimpMember.setMergeFields(mergeFields);

        return mailChimpMember;
    }
}
