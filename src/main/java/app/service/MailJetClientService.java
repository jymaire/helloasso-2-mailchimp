package app.service;

import app.gui.MainWindow;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.stereotype.Service;

@Service
public class MailJetClientService {

    private MailjetClient mailJetClient = null;

    public MailJetClientService() {
    }

    public MailjetClient getMailJetClient() {
        if (this.mailJetClient == null) {
            ClientOptions options = ClientOptions.builder()
                    .apiKey(MainWindow.properties.getProperty("MAIL_JET_API_KEY"))
                    .apiSecretKey(MainWindow.properties.getProperty("MAIL_JET_API_SECRET"))
                    .baseUrl(MainWindow.properties.getProperty("MAIL_JET_API_URL"))
                    .build();
            this.mailJetClient = new MailjetClient(options);
        }
        return this.mailJetClient;

    }


}
