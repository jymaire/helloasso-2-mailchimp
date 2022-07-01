package re.jmai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class HelloAsso2MailChimpApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloAsso2MailChimpApplication.class, args);
    }

}
