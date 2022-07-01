package app.bean.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MailChimpToken {
    @JsonProperty("health_status")
    private String health_status;

    public String getHealth_status() {
        return health_status;
    }

    public void setHealth_status(String health_status) {
        this.health_status = health_status;
    }

    @Override
    public String toString() {
        return "MailChimpToken{" +
                "health_status='" + health_status + '\'' +
                '}';
    }
}
