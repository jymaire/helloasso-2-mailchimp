package re.jmai.entity;


import re.jmai.bean.StatusPaymentEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

import static re.jmai.bean.StatusPaymentEnum.todo;


@Entity
public class HelloAssoPaymentEntity {
    public static final int ERROR_LENGTH = 700;
    @Id
    private String id;
    private String date;
    // in euro
    private float amount;
    private String payerFirstName;
    private String payerLastName;
    // technical field, to handle purge process
    private LocalDateTime insertionDate;
    private StatusPaymentEnum status;
    @Column(length = ERROR_LENGTH)
    private String error;
    private String email;
    private String tarif;
    private String codePostal;
    private String entrepriseProjet;
    private String helloAssoStatus;


    public HelloAssoPaymentEntity() {
    }

    public HelloAssoPaymentEntity(String id, String date, float amount, String payerFirstName, String payerLastName, String email) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.payerFirstName = payerFirstName;
        this.payerLastName = payerLastName;
        this.insertionDate = LocalDateTime.now();
        this.status = todo;
        this.error = "";
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public StatusPaymentEnum getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(LocalDateTime insertionDate) {
        this.insertionDate = insertionDate;
    }

    public void setStatus(StatusPaymentEnum status) {
        this.status = status;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getEntrepriseProjet() {
        return entrepriseProjet;
    }

    public void setEntrepriseProjet(String entrepriseProjet) {
        this.entrepriseProjet = entrepriseProjet;
    }

    public String getHelloAssoStatus() {
        return helloAssoStatus;
    }

    public void setHelloAssoStatus(String helloAssoStatus) {
        this.helloAssoStatus = helloAssoStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HelloAssoPaymentEntity payment = (HelloAssoPaymentEntity) o;
        return id == payment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                '}';
    }


    public static final class HelloAssoPaymentEntityBuilder {
        private String id;
        private String date;
        // in euro
        private float amount;
        private String payerFirstName;
        private String payerLastName;
        // technical field, to handle purge process
        private LocalDateTime insertionDate;
        private StatusPaymentEnum status;
        private String error;
        private String email;
        private String tarif;
        private String codePostal;
        private String entrepriseProjet;
        private String helloAssoStatus;

        private HelloAssoPaymentEntityBuilder() {
        }

        public static HelloAssoPaymentEntityBuilder aHelloAssoPaymentEntity() {
            return new HelloAssoPaymentEntityBuilder();
        }

        public HelloAssoPaymentEntityBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withDate(String date) {
            this.date = date;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withAmount(float amount) {
            this.amount = amount;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withPayerFirstName(String payerFirstName) {
            this.payerFirstName = payerFirstName;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withPayerLastName(String payerLastName) {
            this.payerLastName = payerLastName;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withInsertionDate(LocalDateTime insertionDate) {
            this.insertionDate = insertionDate;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withStatus(StatusPaymentEnum status) {
            this.status = status;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withError(String error) {
            this.error = error;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withTarif(String tarif) {
            this.tarif = tarif;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withCodePostal(String codePostal) {
            this.codePostal = codePostal;
            return this;
        }

        public HelloAssoPaymentEntityBuilder withEntrepriseProjet(String entrepriseProjet) {
            this.entrepriseProjet = entrepriseProjet;
            return this;
        }
        public HelloAssoPaymentEntityBuilder withHelloAssoStatus(String helloAssoStatus) {
            this.helloAssoStatus = helloAssoStatus;
            return this;
        }
        public HelloAssoPaymentEntity build() {
            HelloAssoPaymentEntity helloAssoPaymentEntity = new HelloAssoPaymentEntity(id, date, amount, payerFirstName, payerLastName, email);
            helloAssoPaymentEntity.setInsertionDate(insertionDate);
            helloAssoPaymentEntity.setStatus(status);
            helloAssoPaymentEntity.setError(error);
            helloAssoPaymentEntity.setTarif(tarif);
            helloAssoPaymentEntity.setCodePostal(codePostal);
            helloAssoPaymentEntity.setEntrepriseProjet(entrepriseProjet);
            return helloAssoPaymentEntity;
        }
    }
}
