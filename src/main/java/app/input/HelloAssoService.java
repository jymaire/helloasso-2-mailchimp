package app.input;

import app.gui.MainWindow;
import app.model.HelloAssoFormPayments;
import app.model.HelloAssoToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.netty.http.client.HttpClient;

import java.time.LocalDateTime;
import java.util.Properties;

@Service
public class HelloAssoService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String getHelloAssoAccessToken(Properties properties) throws IllegalAccessException {
        MultiValueMap accessTokenBody = new LinkedMultiValueMap();
        accessTokenBody.add("client_id", properties.getProperty("HELLO_ASSO_CLIENT_ID"));
        accessTokenBody.add("client_secret", properties.getProperty("HELLO_ASSO_CLIENT_SECRET"));
        accessTokenBody.add("grant_type", "client_credentials");
        HttpClient httpClient = HttpClient
                .create()
                .wiretap(true);
        HelloAssoToken accessTokenResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().post()
                .uri(properties.getProperty("HELLO_ASSO_API_URL") + "oauth2/token")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(accessTokenBody))
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(HelloAssoToken.class)
                .block();

        if (accessTokenResponse == null || accessTokenResponse.getAccess_token() == null) {
            LOGGER.error("Erreur lors de la récupération du token : \n{}", accessTokenResponse);
            throw new IllegalAccessException("First access token error");
        }
        return accessTokenResponse.getAccess_token();
    }

    public ResponseEntity<HelloAssoFormPayments> callPaymentFormHistory(String accessToken, LocalDateTime now, LocalDateTime beginDate, Properties properties) {
        return WebClient.builder().build().get()
                .uri(properties.getProperty("HELLO_ASSO_API_URL") + "v5/organizations/" + properties.getProperty("HELLO_ASSO_ORGANIZATION") +
                        "/forms/PaymentForm/" + properties.getProperty("HELLO_ASSO_FORM") + "/payments?from=" + beginDate + "&to=" + now + "&states=Authorized")
                .header("authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(HelloAssoFormPayments.class)
                .block();
    }

    public void disconnect(String token, Properties properties) {
        final WebClient.ResponseSpec retrieve = WebClient.builder().build().get()
                .uri(properties.getProperty("HELLO_ASSO_API_URL") + "oauth2/disconnect")
                .header("authorization", "Bearer " + token)
                .retrieve();
    }

    public void getPaymentsFor(int nbDays) throws IllegalAccessException {
        final Properties properties = MainWindow.properties;

        String token = getHelloAssoAccessToken(properties);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDate = now.minusDays(nbDays);

        ResponseEntity<HelloAssoFormPayments> formResponse;
        try {
            formResponse = callPaymentFormHistory(token, now, beginDate,properties);
            System.out.println(formResponse.getBody().getData());
        } catch (WebClientException exception) {
            LOGGER.error("error during data fetch : {}", exception.getCause().getMessage());
            return;
        } finally {
            disconnect(token,properties);
        }

    }
}
