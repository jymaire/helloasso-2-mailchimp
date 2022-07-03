package app.bean.helloasso.notification;

import app.bean.helloasso.HelloAssoOrderItem;
import app.bean.helloasso.HelloAssoPayer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloAssoOrderNotificationBody {

    private List<HelloAssoOrderItem> items;
    private HelloAssoPayer payer;
    private HelloAssoAmount amount;
    private String id;
    private String date;
    private String formSlug;
    private String state;

    public HelloAssoOrderNotificationBody() {
    }

    public List<HelloAssoOrderItem> getItems() {
        return items;
    }

    public void setItems(List<HelloAssoOrderItem> items) {
        this.items = items;
    }

    public HelloAssoPayer getPayer() {
        return payer;
    }

    public void setPayer(HelloAssoPayer payer) {
        this.payer = payer;
    }

    public HelloAssoAmount getAmount() {
        return amount;
    }

    public void setAmount(HelloAssoAmount amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormSlug() {
        return formSlug;
    }

    public void setFormSlug(String formSlug) {
        this.formSlug = formSlug;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "HelloAssoPaymentNotificationBody{" +
                "items=" + items +
                ", amount=" + amount +
                ", id='" + id + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
