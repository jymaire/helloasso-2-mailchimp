package re.jmai.bean;

public class AdminConfig {

    private boolean paymentAutomaticEnabled;
    private String mailRecipient;

    public AdminConfig(boolean paymentAutomaticEnabled, String mailRecipient) {
        this.paymentAutomaticEnabled = paymentAutomaticEnabled;
        this.mailRecipient = mailRecipient;
    }

    public boolean isPaymentAutomaticEnabled() {
        return paymentAutomaticEnabled;
    }

    public void setPaymentAutomaticEnabled(boolean paymentAutomaticEnabled) {
        this.paymentAutomaticEnabled = paymentAutomaticEnabled;
    }

    public String getMailRecipient() {
        return mailRecipient;
    }

    public void setMailRecipient(String mailRecipient) {
        this.mailRecipient = mailRecipient;
    }
}
