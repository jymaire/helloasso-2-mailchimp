package app.model.mailchimp;

public class MailChimpMember {
    private String id;
    private String emailAddress;
    private String uniqueEmailId;
    private String contactId;
    private String fullName;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUniqueEmailId() {
        return uniqueEmailId;
    }

    public void setUniqueEmailId(String uniqueEmailId) {
        this.uniqueEmailId = uniqueEmailId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MailChimpMember{" +
                "id='" + id + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", uniqueEmailId='" + uniqueEmailId + '\'' +
                ", contactId='" + contactId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
