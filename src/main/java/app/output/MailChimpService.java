package app.output;

import app.gui.MainWindow;
import app.model.mailchimp.MailChimpMemberList;
import app.model.mailchimp.MailChimpToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class MailChimpService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Properties properties;

    public void getPaymentsFor(int nbDays) throws IllegalAccessException {

        this.properties = MainWindow.properties;

        String token = getMailChimpAccessToken();
    }

    private String getMailChimpAccessToken() throws IllegalAccessException {
        MultiValueMap accessTokenBody = new LinkedMultiValueMap();
        accessTokenBody.add("server", properties.getProperty("MAIL_CHIMP_DC"));
        accessTokenBody.add("apiKey", properties.getProperty("MAIL_CHIMP_API_KEY"));
        HttpClient httpClient = HttpClient
                .create()
                .wiretap(true);
        MailChimpMemberList accessTokenResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().get()
        //        .uri(properties.getProperty("MAIL_CHIMP_API_URL") + "lists")
                .uri(properties.getProperty("MAIL_CHIMP_API_URL") + "lists/" + properties.getProperty("MAIL_CHIMP_LIST_ID") + "/members")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("username" + ":" + properties.getProperty("MAIL_CHIMP_API_KEY")).getBytes(UTF_8))).retrieve()
                .bodyToMono(MailChimpMemberList.class)
                .block();

        System.out.println(accessTokenBody);
        return accessTokenBody.toString();
    }

    public void disconnect(String token) {
        final WebClient.ResponseSpec retrieve = WebClient.builder().build().get()
                .uri(properties.getProperty("HELLO_ASSO_API_URL") + "oauth2/disconnect")
                .header("authorization", "Bearer " + token)
                .retrieve();
    }
}
