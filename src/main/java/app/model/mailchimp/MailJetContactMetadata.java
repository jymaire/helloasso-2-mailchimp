package app.model.mailchimp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/*
{ "Count" : 1, "Data" :
[{ "ContactID" : 3242563236,
"Data" : [{ "Name" : "prénom", "Value" : "Jérémy" },
{ "Name" : "nom", "Value" : "Maire" },
{ "Name" : "formule", "Value" : "NORMAL" },
{ "Name" : "date", "Value" : "2022-04-26T00:00:00Z" },
{ "Name" : "paiement", "Value" : "Espèce" },
{ "Name" : "depuis", "Value" : "2022" },
{ "Name" : "postal", "Value" : "69009" }],
"ID" : 3242563236, "MethodCollection" : "" }],
"Total" : 1 }%
 */
public class MailJetContactMetadata {
    @JsonProperty("Data")
    private HashMap<String,String> listOfMmetadata = new HashMap<>();

    public MailJetContactMetadata() {
    }

    public MailJetContactMetadata(HashMap<String, String> listOfMmetadata) {
        this.listOfMmetadata = listOfMmetadata;
    }

    public HashMap<String, String> getListOfMmetadata() {
        return listOfMmetadata;
    }

    public void setListOfMmetadata(HashMap<String, String> listOfMmetadata) {
        this.listOfMmetadata = listOfMmetadata;
    }

    @Override
    public String toString() {
        return "MailJetContactMetadata{" +
                "listOfMmetadata=" + listOfMmetadata +
                '}';
    }
}
