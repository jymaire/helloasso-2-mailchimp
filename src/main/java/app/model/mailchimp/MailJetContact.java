package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class MailJetContact {
    @JsonProperty("Email")
    private String email;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("IsExcludedFromCampaigns")
    private boolean isExcluded = false;
    public MailJetContact() {
    }

    public MailJetContact(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExcluded() {
        return isExcluded;
    }

    public void setExcluded(boolean excluded) {
        isExcluded = excluded;
    }

    @Override
    public String toString() {
        return "MailJetContact{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
