package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MailJetContactSearchResult {

    @JsonProperty("Count")
     private Integer count;

    @JsonProperty("Data")
    private List<MailJetContact> contact;

    public MailJetContactSearchResult(Integer count, List<MailJetContact> contact) {
        this.count = count;
        this.contact = contact;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<MailJetContact> getContact() {
        return contact;
    }

    public void setContact(List<MailJetContact> contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "MailJetContactSearchResult{" +
                "count=" + count +
                ", contact=" + contact +
                '}';
    }
}
