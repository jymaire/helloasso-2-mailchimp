package app.output;

import app.gui.MainWindow;
import app.model.mailchimp.MailJetContact;
import app.model.mailchimp.MailJetContactMetadata;
import app.service.MailJetClientService;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetClientRequestException;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Contact;
import com.mailjet.client.resource.Contactdata;
import com.mailjet.client.resource.ContactslistManageContact;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MailJetService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private MailJetClientService mailJetClientService;

    public MailJetService(MailJetClientService mailJetClientService) {
        this.mailJetClientService = mailJetClientService;
    }

    public String addMembers(Map<String, MailJetContact> mailJetContactMap, Map<String, MailJetContactMetadata> mailJetContactMetadataMap) {
        List<Object> results = new ArrayList<>();

        for (String email : mailJetContactMap.keySet()) {
            LOGGER.info("member to post : {}", mailJetContactMap.get(email));
            LOGGER.info("member to post : {}", mailJetContactMetadataMap.get(email));

            Object postResponse = addOneMember(mailJetContactMap.get(email), mailJetContactMetadataMap.get(email));
            results.add(postResponse);
            if (postResponse != null) {
                LOGGER.debug(postResponse.toString());
            } else {
                LOGGER.debug("postresponse is null");
            }
        }


        return results.toString();
    }

    private Object addOneMember(MailJetContact contact, MailJetContactMetadata metadata) {
        // ajouter "depuis" avec année en cours comme valeur
        boolean memberAddedSuccessfully = false;
        try {
            MailjetRequest addUserRequest = new MailjetRequest(Contact.resource)
                    .property(Contact.ISEXCLUDEDFROMCAMPAIGNS, "false")
                    .property(Contact.NAME, contact.getEmail())
                    .property(Contact.EMAIL, contact.getEmail());
            boolean creationOfUser = true;
            try {
                final MailjetResponse contactCreationResponse = mailJetClientService.getMailJetClient().post(addUserRequest);

            } catch (MailjetClientRequestException e) {
                creationOfUser = false;
            }

            addMetadata(metadata, creationOfUser);


            // ajout à la liste de contact
            final MailjetRequest addToListRequet = new MailjetRequest(ContactslistManageContact.resource, MainWindow.properties.getProperty("MAIL_JET_LIST_ADHESION"))
                    //.property(ContactslistManageContact.NAME, "John Smith")
                    .property(ContactslistManageContact.PROPERTIES, "object")
                    .property(ContactslistManageContact.ACTION, "addnoforce")
                    .property(ContactslistManageContact.EMAIL, contact.getEmail());

            final MailjetResponse addToContactListResponse = mailJetClientService.getMailJetClient().post(addToListRequet);
            System.out.println(addToContactListResponse);
        } catch (MailjetException ex) {
            throw new RuntimeException(ex);

        } catch (Exception e) {
            LOGGER.error("Une erreur est apparu : {}", e.getMessage());
            return null;
        } finally {
            MainWindow.importResult.append("<br/>");
            MainWindow.importResult.append("Ajout de l'adhérent ");
            MainWindow.importResult.append(contact.getEmail());
            MainWindow.importResult.append(" réussi : ");
            if (memberAddedSuccessfully) {
                MainWindow.importResult.append("oui");
            } else {
                MainWindow.importResult.append("non");
            }
        }
        return null;
    }

    private void addMetadata(MailJetContactMetadata metadata, boolean creationOfUser) throws MailjetException {
        extracted(metadata, "nom");
        extracted(metadata, "prénom");
        extracted(metadata, "date");
        extracted(metadata, "formule");
        extracted(metadata, "postal");
        if (creationOfUser) {
            MailjetRequest metadataCreationRequest = new MailjetRequest(Contactdata.resource, metadata.getListOfMmetadata().get("email"))
                    .property(Contactdata.DATA, new JSONArray()
                            .put(new JSONObject()
                                    .put("Name", "depuis")
                                    .put("Value", Year.now())
                            )
                    );
            mailJetClientService.getMailJetClient().put(metadataCreationRequest);
        }
    }

    private boolean extracted(MailJetContactMetadata metadata, String propertyName) throws MailjetException {
        MailjetRequest metadataCreationRequest = new MailjetRequest(Contactdata.resource, metadata.getListOfMmetadata().get("email"))
                .property(Contactdata.DATA, new JSONArray()
                        .put(new JSONObject()
                                .put("Name", propertyName)
                                .put("Value", metadata.getListOfMmetadata().get(propertyName))
                        )
                );
        final MailjetResponse metatadataReturn = mailJetClientService.getMailJetClient().put(metadataCreationRequest);
        if (metatadataReturn.getStatus() == 201) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isMemberPresent(String emailHash) throws MailjetException {
        MailjetRequest request = new MailjetRequest(Contact.resource)
                .filter(Contact.EMAIL, emailHash);
        final MailjetResponse mailjetResponse = mailJetClientService.getMailJetClient().get(request);
        System.out.println(mailjetResponse);
        if (mailjetResponse == null || mailjetResponse.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
