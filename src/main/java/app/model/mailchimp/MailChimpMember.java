package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class MailChimpMember {
    @JsonProperty("email_address")
    private String email_address;
    @JsonProperty("email_type")
    private String email_type;
    @JsonProperty("status")
    private String status;

    @JsonProperty("merge_fields")
    private HashMap<String, Object> merge_fields;


    public String getEmail_type() {
        return email_type;
    }

    public void setEmail_type(String email_type) {
        this.email_type = email_type;
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

    public HashMap<String, Object> getMerge_fields() {
        return merge_fields;
    }

    public void setMerge_fields(HashMap<String, Object> merge_fields) {
        this.merge_fields = merge_fields;
    }

    @Override
    public String toString() {
        return "MailChimpMember{" +
                "email='" + email_address + '\'' +
                ", email_type='" + email_type + '\'' +
                ", status='" + status + '\'' +
                ", merge_fields=" + merge_fields +
                '}';
    }
}
