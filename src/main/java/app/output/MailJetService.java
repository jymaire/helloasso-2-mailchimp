package app.output;

import app.gui.MainWindow;
import app.model.mailchimp.MailJetContact;
import app.model.mailchimp.MailJetContactMetadata;
import app.service.MailJetClientService;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            String emailHash = "";
            emailHash = commuteEmailHash(contact.getEmail().toLowerCase());
            boolean isMemberPresent = isMemberPresent(contact.getEmail());
            System.out.println(isMemberPresent);
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


    private String commuteEmailHash(String email) {
        String hashtext = "";
        try {
            byte[] digest;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(email.getBytes("UTF-8"));
            digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            LOGGER.debug("email hash : {}", hashtext);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return hashtext;
    }
}
