package app.output;

import app.gui.MainWindow;
import app.model.mailchimp.MailChimpMember;
import app.model.mailchimp.MailChimpMemberUpdate;
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

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class MailChimpService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

    private Object addOneMember(MailChimpMember newMember) {
        HttpClient httpClient = HttpClient
                .create()
                .wiretap(true);
        String emailHash = "";
        emailHash = commuteEmailHash(newMember.getEmail());
        boolean isMemberPresent = isMemberPresent(httpClient, emailHash);
        LOGGER.info("Member already there ? {}", isMemberPresent);
        Object postResponse;
        if (isMemberPresent) {
            MailChimpMemberUpdate mailChimpMemberUpdate = MailChimpMemberUpdate.MailChimpMemberUpdateBuilder.aMailChimpMemberUpdate()
                    .withEmail(newMember.getEmail())
                    .withEmailType(newMember.getEmailType())
                    .withMergeFields(newMember.getMergeFields())
                    .withStatus(newMember.getStatus())
                    .withStatusIfNew("subscribed")
                    .build();
            postResponse = updateMember(mailChimpMemberUpdate, httpClient, emailHash);
            LOGGER.info("maj : {}", postResponse);
        } else {
            postResponse = createMember(newMember, httpClient);
            LOGGER.info("creation : {}", postResponse);
        }
        return postResponse;
    }

    private boolean isMemberPresent(HttpClient httpClient, String emailHash) {
        boolean isPresent = true;
        try {
            WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient)).build().get()
                    .uri(MainWindow.properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + MainWindow.properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members/" + emailHash)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + Base64Utils
                            .encodeToString(("username" + ":" + MainWindow.properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                    .bodyToMono(Object.class)
                    .block();
        } catch (WebClientException e) {
            if (((WebClientResponseException.NotFound) e).getStatusCode().value() == 404) {
                isPresent = false;
            }
        }
        return isPresent;
    }

    private Object updateMember(MailChimpMemberUpdate memberUpdate, HttpClient httpClient, String emailHash) {
        Object postResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().put()
                .uri(MainWindow.properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + MainWindow.properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members/" + emailHash)
                .bodyValue(memberUpdate)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username" + ":" + MainWindow.properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                .bodyToMono(Object.class)
                .block();
        return postResponse;
    }

    private Object createMember(MailChimpMember newMember, HttpClient httpClient) {
        Object postResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().post()
                .uri(MainWindow.properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + MainWindow.properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members")
                .bodyValue(newMember)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username" + ":" + MainWindow.properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
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
