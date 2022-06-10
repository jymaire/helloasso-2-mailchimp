package app.output;

import app.model.mailchimp.MailChimpMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class MailChimpService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Properties properties;

    public String addMembers(List<MailChimpMember> memberList) {

        List<Object> results = new ArrayList<>();
        for (MailChimpMember mailChimpMember : memberList) {
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

}
