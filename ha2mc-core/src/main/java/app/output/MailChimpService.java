package app.output;

import app.bean.mailchimp.MailChimpMember;
import app.bean.mailchimp.MailChimpMemberUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class MailChimpService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private Properties properties;
    private StringBuilder importResult;

    public MailChimpService(Properties properties, StringBuilder importResult) {
        this.properties = properties;
        this.importResult = importResult;
    }

    public String addMembers(List<MailChimpMember> memberList) {

        List<Object> results = new ArrayList<>();
        for (MailChimpMember mailChimpMember : memberList) {
            LOGGER.info("member to post : {}", mailChimpMember);
            Object postResponse = addOneMember(mailChimpMember);
            results.add(postResponse);
            LOGGER.debug(postResponse.toString());
        }

        return results.toString();
    }

    public Object addOneMember(MailChimpMember newMember) {
        boolean memberAddedSuccessfully = false;
        try {
            HttpClient httpClient = HttpClient
                    .create()
                    .wiretap(true);
            String emailHash = "";
            emailHash = commuteEmailHash(newMember.getEmail_address().toLowerCase());
            boolean isMemberPresent = isMemberPresent(httpClient, emailHash);
            LOGGER.info("Member already there ? {}", isMemberPresent);
            Object postResponse = null;
            if (isMemberPresent) {
                MailChimpMemberUpdate mailChimpMemberUpdate = MailChimpMemberUpdate.MailChimpMemberUpdateBuilder.aMailChimpMemberUpdate()
                        .withEmail(newMember.getEmail_address())
                        .withEmailType(newMember.getEmailType())
                        .withMergeFields(newMember.getMergeFields())
                        .withStatus(newMember.getStatus())
                        .withStatusIfNew("subscribed")
                        .build();
                postResponse = updateMember(mailChimpMemberUpdate, httpClient, emailHash);
                LOGGER.info("maj : {}", postResponse);
            } else {
                try {
                    postResponse = createMember(newMember, httpClient);
                    LOGGER.info("creation : {}", postResponse);
                } catch (Exception e) {
                    LOGGER.error("Erreur lors de la création : {}", e.getMessage());
                }
            }
            memberAddedSuccessfully = true;

            return postResponse;
        } catch (Exception e) {
            LOGGER.error("Une erreur est apparu : {}", e.getMessage());
            return null;
        } finally {
            importResult.append("<br/>");
            importResult.append("Ajout de l'adhérent ");
            importResult.append(newMember.getEmail_address());
            importResult.append(" réussi : ");
            if (memberAddedSuccessfully) {
                importResult.append("oui");
            } else {
                importResult.append("non");
            }
        }
    }

    private boolean isMemberPresent(HttpClient httpClient, String emailHash) {
        boolean isPresent = true;

        try {
            Object result = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient)).build().get()
                    .uri(properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members/" + emailHash)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString(("username" + ":" + properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                    .bodyToMono(Object.class)
                    .block();
            LOGGER.debug(result.toString());
        } catch (WebClientException e) {
            if (e instanceof WebClientResponseException && ((WebClientResponseException.NotFound) e).getStatusCode().value() == 404) {
                isPresent = false;
            } else {
                throw e;
            }
        }
        return isPresent;
    }

    private Object updateMember(MailChimpMemberUpdate memberUpdate, HttpClient httpClient, String emailHash) {
        Object postResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().put()
                .uri(properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members/" + emailHash)
                .bodyValue(memberUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username" + ":" + properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                .bodyToMono(Object.class)
                .block();
        return postResponse;
    }

    private Object createMember(MailChimpMember newMember, HttpClient httpClient) {
        Object postResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().post()
                .uri(properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members")
                .bodyValue(newMember)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username" + ":" + properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                .bodyToMono(Object.class)
                .block();
        return postResponse;
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
