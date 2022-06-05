package app.input;

import app.gui.MainWindow;
import app.model.XlsxModel;
import app.model.helloasso.*;
import app.process.ConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.netty.http.client.HttpClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HelloAssoService {
    public static final String TARIF = "tarif";
    public static final String ENTREPRISE = "entreprise";
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private ConvertService convertService;
    private Properties properties;

    public HelloAssoService(ConvertService convertService) {
        this.convertService = convertService;
    }

    private String getHelloAssoAccessToken() throws IllegalAccessException {
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

    public ResponseEntity<HelloAssoFormPayments> callPaymentFormHistory(String accessToken, LocalDateTime now, LocalDateTime beginDate) {
        // TODO Membership au lieu de PaymentForm pour ZDL
        return WebClient.builder().build().get()
                .uri(properties.getProperty("HELLO_ASSO_API_URL") + "v5/organizations/" + properties.getProperty("HELLO_ASSO_ORGANIZATION") +
                        "/forms/PaymentForm/" + properties.getProperty("HELLO_ASSO_FORM") + "/payments?from=" + beginDate + "&to=" + now + "&states=Authorized")
                .header("authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(HelloAssoFormPayments.class)
                .block();
    }

    public void disconnect(String token) {
        final WebClient.ResponseSpec retrieve = WebClient.builder().build().get()
                .uri(properties.getProperty("HELLO_ASSO_API_URL") + "oauth2/disconnect")
                .header("authorization", "Bearer " + token)
                .retrieve();
    }

    public void getPaymentsFor(int nbDays) throws IllegalAccessException {
        this.properties = MainWindow.properties;

        String token = getHelloAssoAccessToken();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDate = now.minusDays(nbDays);

        ResponseEntity<HelloAssoFormPayments> formResponse;
        try {
            formResponse = callPaymentFormHistory(token, now, beginDate);

            if (formResponse.getBody().getData() != null) {
                List<XlsxModel> xlsxModels = new ArrayList<>();

                for (HelloAssoPayment helloAssoPayment : formResponse.getBody().getData()) {
                    // besoin des champs additionnels
                    final Map<String, String> extraFields = getExtraFields(helloAssoPayment.getOrder().getId(), token);

                    XlsxModel xlsxModel = new XlsxModel();
                    xlsxModel.setDate(LocalDateTime.parse(helloAssoPayment.getDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    xlsxModel.setEmail(helloAssoPayment.getPayer().getEmail());
                    xlsxModel.setCodePostal(helloAssoPayment.getPayer().getZipCode());
                    xlsxModel.setNom(helloAssoPayment.getPayer().getLastName());
                    xlsxModel.setPrenom(helloAssoPayment.getPayer().getFirstName());
                    xlsxModel.setStatus(helloAssoPayment.getState().name());
                    xlsxModel.setTarif(extraFields.get(TARIF));
                    xlsxModel.setEntrepriseProjet(extraFields.get(ENTREPRISE));
                    xlsxModels.add(xlsxModel);
                }
                convertService.convert(System.getProperty("user.dir"), xlsxModels);

            }
            System.out.println(formResponse.getBody().getData());
        } catch (WebClientException exception) {
            LOGGER.error("error during data fetch : {}", exception.getCause());
            return;
        } finally {
            disconnect(token);
        }
    }

    private Map<String, String> getExtraFields(int orderId, String token) {
        Map<String, String> extraFields = new HashMap<>();
        final ResponseEntity<HelloAssoOrder> orderResponse = WebClient.builder().build().get()
                .uri(properties.get("HELLO_ASSO_API_URL") + "v5/orders/" + orderId)
                .header("authorization", "Bearer " + token)
                .retrieve()
                .toEntity(HelloAssoOrder.class)
                .block();
        if (orderResponse != null && orderResponse.getStatusCode().is2xxSuccessful() && orderResponse.getBody() != null) {
            final List<HelloAssoOrderItem> items = orderResponse.getBody().getItems();
            if (!CollectionUtils.isEmpty(items)) {
                for (HelloAssoOrderItem item : items) {
                    if (!CollectionUtils.isEmpty(item.getCustomFields())) {
                        for (HelloAssoItemCustomField field : item.getCustomFields()) {
                            if (TARIF.equals(field.getName())) {
                                LOGGER.info("tarif trouvé");
                                extraFields.put(TARIF, field.getAnswer());
                            }
                            if (ENTREPRISE.equals(field.getName())) {
                                LOGGER.info("entreprise trouvé");
                                extraFields.put(ENTREPRISE, field.getAnswer());
                            }
                        }
                    }
                }

            }
        }
        return extraFields;
    }
}
