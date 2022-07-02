package re.jmai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import re.jmai.entity.Configuration;
import re.jmai.repository.ConfigurationRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigurationService {

    public static final String PAYMENT_AUTOMATIC_ENABLED = "PAYMENT_AUTOMATIC_ENABLED";
    public static final String MAIL_RECIPIENT = "MAIL_RECIPIENT";

    private final ConfigurationRepository configurationRepository;

    @Value("${adhesion.automatic.enabled}")
    private String enableAutomatic;

    @Value("${mail.recipient}")
    private String emailRecipient;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;

    }

    public Map<String, String> findAll() {
        return configurationRepository.findAll().stream().collect(Collectors.toMap(Configuration::getConfigKey, Configuration::getValue));
    }

    public void save(String enableAutomatic, String emailRecipient) {
        var configs = new ArrayList<Configuration>();
        configs.add(new Configuration(PAYMENT_AUTOMATIC_ENABLED, enableAutomatic));
        configs.add(new Configuration(MAIL_RECIPIENT, emailRecipient));
        configurationRepository.saveAll(configs);
    }
}
