package app.input;

import app.model.XlsxModel;
import app.model.helloasso.*;
import app.process.ConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    // Tarifs :
    public static final String REDUIT = "RÉDUIT";
    public static final String NORMAL = "NORMAL";
    public static final String PRO = "Professionnels";
    public static final String PROJETS = "Entreprise/Projet";

    public static final String TARIF = "tarif";
    public static final String ENTREPRISE = "entreprise";
    public static final String CODE_POSTAL = "Code Postal";

    private ConvertService convertService;
    private Properties properties;
    private StringBuilder importResult;

    @Value("${HELLO_ASSO_CLIENT_ID}")
    private String helloAssoUrl;
    @Value("${HELLO_ASSO_CLIENT_ID}")
    private String helloAssoClientId;
    @Value("${HELLO_ASSO_CLIENT_SECRET}")
    private String helloAssoClientSecret;
    @Value("${HELLO_ASSO_FORM}")
    private String helloAssoForm;
    @Value("${HELLO_ASSO_ORGANIZATION}")
    private String helloAssoOrganization;
    @Value("${HELLO_ASSO_API_URL}")
    private String helloAssoAPIurl;

    public HelloAssoService(ConvertService convertService, Properties properties, StringBuilder importResult) {
        this.convertService = convertService;
        this.properties = properties;
        this.importResult = importResult;
    }

    private String getHelloAssoAccessToken() throws IllegalAccessException {
        MultiValueMap accessTokenBody = new LinkedMultiValueMap();
        accessTokenBody.add("client_id", helloAssoClientId);
        accessTokenBody.add("client_secret", helloAssoClientSecret);
        accessTokenBody.add("grant_type", "client_credentials");
        HttpClient httpClient = HttpClient
                .create()
                .wiretap(true);
        HelloAssoToken accessTokenResponse = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build().post()
                .uri(helloAssoUrl + "oauth2/token")
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
        return WebClient.builder().build().get()
                .uri(helloAssoUrl + "v5/organizations/" + helloAssoOrganization +
                        "/forms/Membership/" + helloAssoForm + "/payments?from=" + beginDate + "&to=" + now + "&states=Authorized")
                .header("authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(HelloAssoFormPayments.class)
                .block();
    }

    public void disconnect(String token) {
        final WebClient.ResponseSpec retrieve = WebClient.builder().build().get()
                .uri(helloAssoUrl + "oauth2/disconnect")
                .header("authorization", "Bearer " + token)
                .retrieve();
    }

    public void getPaymentsFor(int nbDays) throws IllegalAccessException {
        String token = getHelloAssoAccessToken();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDate = now.minusDays(nbDays);

        ResponseEntity<HelloAssoFormPayments> formResponse;
        try {
            formResponse = callPaymentFormHistory(token, now, beginDate);

            if (formResponse.getBody().getData() != null) {
                List<XlsxModel> xlsxModels = new ArrayList<>();
                importResult.append("Les adhésions suivantes ont été trouvées dans Hello Asso : ");

                for (HelloAssoPayment helloAssoPayment : formResponse.getBody().getData()) {
                    // besoin des champs additionnels
                    final Map<String, String> extraFields = getExtraFields(helloAssoPayment.getOrder().getId(), token);

                    XlsxModel xlsxModel = new XlsxModel();
                    xlsxModel.setDate(LocalDateTime.parse(helloAssoPayment.getDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    xlsxModel.setEmail(helloAssoPayment.getPayer().getEmail());
                    xlsxModel.setCodePostal(extraFields.get(CODE_POSTAL));
                    xlsxModel.setNom(helloAssoPayment.getPayer().getLastName());
                    xlsxModel.setPrenom(helloAssoPayment.getPayer().getFirstName());
                    xlsxModel.setStatus(helloAssoPayment.getState().name());
                    xlsxModel.setTarif(extraFields.get(TARIF));
                    xlsxModel.setEntrepriseProjet(extraFields.get(ENTREPRISE));
                    xlsxModels.add(xlsxModel);
                    importResult.append("<br/>");
                    importResult.append(xlsxModel);

                }
                convertService.convert(System.getProperty("user.dir"), xlsxModels);

            }else {
                importResult.append("Aucune résultat trouvé dans Hello Asso");
            }
            LOGGER.info(formResponse.getBody().getData().toString());
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
                .uri(helloAssoAPIurl + "v5/orders/" + orderId)
                .header("authorization", "Bearer " + token)
                .retrieve()
                .toEntity(HelloAssoOrder.class)
                .block();
        if (orderResponse != null && orderResponse.getStatusCode().is2xxSuccessful() && orderResponse.getBody() != null) {
            final List<HelloAssoOrderItem> items = orderResponse.getBody().getItems();
            if (!CollectionUtils.isEmpty(items)) {
                for (HelloAssoOrderItem item : items) {
                    if (REDUIT.equals(item.getName()) || PRO.equals(item.getName()) || NORMAL.equals(item.getName())) {
                        LOGGER.debug("tarif trouvé");
                        extraFields.put(TARIF, item.getName());
                    }
                    if (!CollectionUtils.isEmpty(item.getCustomFields())) {
                        for (HelloAssoItemCustomField field : item.getCustomFields()) {
                            if (PROJETS.equals(field.getName())) {
                                LOGGER.debug("entreprise trouvée");
                                extraFields.put(ENTREPRISE, field.getAnswer());
                            }
                            if (CODE_POSTAL.equals(field.getName())) {
                                LOGGER.debug("code postal trouvée");
                                extraFields.put(CODE_POSTAL, field.getAnswer());
                            }
                        }
                    }
                }

            }
        }
        return extraFields;
    }
}
