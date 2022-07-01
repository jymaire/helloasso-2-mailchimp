package app.bean.helloasso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloAssoOrder {

    private HelloAssoPayer payer;
    private List<HelloAssoOrderItem> items;
    private List<HelloAssoPayment> payments;
    private int id;


    public HelloAssoOrder() {
    }

    public HelloAssoPayer getPayer() {
        return payer;
    }

    public void setPayer(HelloAssoPayer payer) {
        this.payer = payer;
    }

    public List<HelloAssoOrderItem> getItems() {
        return items;
    }

    public void setItems(List<HelloAssoOrderItem> items) {
        this.items = items;
    }

    public List<HelloAssoPayment> getPayments() {
        return payments;
    }

    public void setPayments(List<HelloAssoPayment> payments) {
        this.payments = payments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HelloAssoOrder{" +
                "payer=" + payer +
                ", items=" + items +
                ", payments=" + payments +
                ", id=" + id +
                '}';
    }
}
