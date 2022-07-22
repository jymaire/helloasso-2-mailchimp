package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MailJetMetadataSearchResult {

    @JsonProperty("Count")
     private Integer count;

    @JsonProperty("Data")
    private List<MailJetContactMetadata> metadata;

    public MailJetMetadataSearchResult(Integer count, List<MailJetContactMetadata> metadata) {
        this.count = count;
        this.metadata = metadata;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<MailJetContactMetadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MailJetContactMetadata> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "MailJetMetadataSearchResult{" +
                "count=" + count +
                ", metadata=" + metadata +
                '}';
    }
}
