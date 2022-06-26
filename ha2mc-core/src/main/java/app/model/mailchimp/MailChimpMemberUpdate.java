package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class MailChimpMemberUpdate {
    @JsonProperty("email_address")
    private String email;
    @JsonProperty("email_type")
    private String emailType;
    @JsonProperty("status")
    private String status;

    @JsonProperty("status_if_new")
    private String statusIfNew;
    @JsonProperty("merge_fields")
    private HashMap<String,Object> mergeFields;


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


    public String getStatusIfNew() {
        return statusIfNew;
    }

    public void setStatusIfNew(String statusIfNew) {
        this.statusIfNew = statusIfNew;
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
                ", statusIfNew='" + statusIfNew + '\'' +
                ", mergeFields=" + mergeFields +
                '}';
    }

    public static final class MailChimpMemberUpdateBuilder {
        private String email;
        private String emailType;
        private String status;
        private String statusIfNew;
        private HashMap<String,Object> mergeFields;

        private MailChimpMemberUpdateBuilder() {
        }

        public static MailChimpMemberUpdateBuilder aMailChimpMemberUpdate() {
            return new MailChimpMemberUpdateBuilder();
        }

        public MailChimpMemberUpdateBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public MailChimpMemberUpdateBuilder withEmailType(String emailType) {
            this.emailType = emailType;
            return this;
        }

        public MailChimpMemberUpdateBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public MailChimpMemberUpdateBuilder withStatusIfNew(String statusIfNew) {
            this.statusIfNew = statusIfNew;
            return this;
        }

        public MailChimpMemberUpdateBuilder withMergeFields(HashMap<String, Object> mergeFields) {
            this.mergeFields = mergeFields;
            return this;
        }

        public MailChimpMemberUpdate build() {
            MailChimpMemberUpdate mailChimpMemberUpdate = new MailChimpMemberUpdate();
            mailChimpMemberUpdate.setEmail(email);
            mailChimpMemberUpdate.setEmailType(emailType);
            mailChimpMemberUpdate.setStatus(status);
            mailChimpMemberUpdate.setStatusIfNew(statusIfNew);
            mailChimpMemberUpdate.setMergeFields(mergeFields);
            return mailChimpMemberUpdate;
        }
    }
}
