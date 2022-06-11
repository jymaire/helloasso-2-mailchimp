package app.output;

import app.gui.MainWindow;
import app.model.mailchimp.MailChimpMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.UnsupportedEncodingException;
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
        String digest = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newMember.getEmail().getBytes("UTF-8"));
            digest = String.valueOf(md.digest()).toLowerCase();
            System.out.println(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Object postResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().put()
                .uri(MainWindow.properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + MainWindow.properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members/" + digest)
                .bodyValue(newMember)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username" + ":" + MainWindow.properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                .bodyToMono(Object.class)
                .block();
        return postResponse;
    }

}
