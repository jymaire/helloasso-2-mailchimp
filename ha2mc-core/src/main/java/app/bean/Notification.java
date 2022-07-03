package app.bean;

public class Notification {

    private String id;
    private String name;
    private String firstName;
    private String email;
    private String formSlug;
    private String date;
    private String state;
    private String codePostal;
    private String tarif;
    private String entrepriseProjet;
    private int amount;

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFormSlug() {
        return formSlug;
    }

    public void setFormSlug(String formSlug) {
        this.formSlug = formSlug;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getEntrepriseProjet() {
        return entrepriseProjet;
    }

    public void setEntrepriseProjet(String entrepriseProjet) {
        this.entrepriseProjet = entrepriseProjet;
    }

    public static final class NotificationBuilder {
        private String id;
        private String name;
        private String firstName;
        private String email;
        private String formSlug;
        private String date;
        private String state;
        private String codePostal;
        private String tarif;
        private String entrepriseProjet;
        private int amount;

        private NotificationBuilder() {
        }

        public static NotificationBuilder aNotification() {
            return new NotificationBuilder();
        }

        public NotificationBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public NotificationBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public NotificationBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public NotificationBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public NotificationBuilder withFormSlug(String formSlug) {
            this.formSlug = formSlug;
            return this;
        }

        public NotificationBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public NotificationBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public NotificationBuilder withCodePostal(String codePostal) {
            this.codePostal = codePostal;
            return this;
        }

        public NotificationBuilder withTarif(String tarif) {
            this.tarif = tarif;
            return this;
        }

        public NotificationBuilder withEntrepriseProjet(String entrepriseProjet) {
            this.entrepriseProjet = entrepriseProjet;
            return this;
        }

        public NotificationBuilder withAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification();
            notification.setId(id);
            notification.setName(name);
            notification.setFirstName(firstName);
            notification.setEmail(email);
            notification.setFormSlug(formSlug);
            notification.setDate(date);
            notification.setState(state);
            notification.setCodePostal(codePostal);
            notification.setTarif(tarif);
            notification.setEntrepriseProjet(entrepriseProjet);
            notification.setAmount(amount);
            return notification;
        }
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", formSlug='" + formSlug + '\'' +
                ", date='" + date + '\'' +
                ", state='" + state + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", tarif='" + tarif + '\'' +
                ", entrepriseProjet='" + entrepriseProjet + '\'' +
                ", amount=" + amount +
                '}';
    }
}
