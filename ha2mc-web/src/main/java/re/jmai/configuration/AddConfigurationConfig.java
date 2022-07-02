package re.jmai.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import re.jmai.repository.ConfigurationRepository;

import java.util.ArrayList;

import static re.jmai.service.ConfigurationService.MAIL_RECIPIENT;
import static re.jmai.service.ConfigurationService.PAYMENT_AUTOMATIC_ENABLED;

@Configuration
public class AddConfigurationConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${adhesion.automatic.enabled}")
    private String enableAutomatic;

    @Value("${mail.recipient}")
    private String emailRecipient;


    @Bean
    public CommandLineRunner demo(ConfigurationRepository repository) {
        return (args) -> {
            var configs = new ArrayList<re.jmai.entity.Configuration>();
            configs.add(new re.jmai.entity.Configuration(PAYMENT_AUTOMATIC_ENABLED, enableAutomatic));
            configs.add(new re.jmai.entity.Configuration(MAIL_RECIPIENT, emailRecipient));
            repository.saveAll(configs);
        };
    }

}
