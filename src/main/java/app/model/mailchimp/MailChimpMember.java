package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class MailChimpMember {
    @JsonProperty("email_address")
    private String email_address;
    @JsonProperty("email_type")
    private String emailType;
    @JsonProperty("status")
    private String status;

    @JsonProperty("merge_fields")
    private HashMap<String, Object> mergeFields;


    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String, Object> getMergeFields() {
        return mergeFields;
    }

    public void setMergeFields(HashMap<String, Object> mergeFields) {
        this.mergeFields = mergeFields;
    }

    @Override
    public String toString() {
        return "MailChimpMember{" +
                "email='" + email_address + '\'' +
                ", emailType='" + emailType + '\'' +
                ", status='" + status + '\'' +
                ", mergeFields=" + mergeFields +
                '}';
    }
}
