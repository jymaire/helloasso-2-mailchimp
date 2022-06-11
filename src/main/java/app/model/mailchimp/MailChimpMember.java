package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class MailChimpMember {
    @JsonProperty("email_address")
    private String email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                "email='" + email + '\'' +
                ", emailType='" + emailType + '\'' +
                ", status='" + status + '\'' +
                ", mergeFields=" + mergeFields +
                '}';
    }
}
