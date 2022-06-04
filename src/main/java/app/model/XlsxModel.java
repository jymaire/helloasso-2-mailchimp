package app.model;

import java.time.LocalDateTime;

public class XlsxModel {
    private LocalDateTime date;
    private String email;
    private String nom;
    private String prenom;
    private String status; // Validé
    private String tarif; //formule dans mailchimp
    private String codePostal;
    private String entrpriseProjet;

    public XlsxModel() {
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getEntrpriseProjet() {
        return entrpriseProjet;
    }

    public void setEntrpriseProjet(String entrpriseProjet) {
        this.entrpriseProjet = entrpriseProjet;
    }

    public static final class XlsxModelBuilder {
        private LocalDateTime date;
        private String email;
        private String nom;
        private String prenom;
        private String status; // Validé
        private String tarif; //formule dans mailchimp
        private String codePostal;
        private String entrpriseProjet;

        private XlsxModelBuilder() {
        }

        public static XlsxModelBuilder aXlsxModel() {
            return new XlsxModelBuilder();
        }

        public XlsxModelBuilder withDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public XlsxModelBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public XlsxModelBuilder withNom(String nom) {
            this.nom = nom;
            return this;
        }

        public XlsxModelBuilder withPrenom(String prenom) {
            this.prenom = prenom;
            return this;
        }

        public XlsxModelBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public XlsxModelBuilder withTarif(String tarif) {
            this.tarif = tarif;
            return this;
        }

        public XlsxModelBuilder withCodePostal(String codePostal) {
            this.codePostal = codePostal;
            return this;
        }

        public XlsxModelBuilder withEntrpriseProjet(String entrpriseProjet) {
            this.entrpriseProjet = entrpriseProjet;
            return this;
        }

        public XlsxModel build() {
            XlsxModel xlsxModel = new XlsxModel();
            xlsxModel.setDate(date);
            xlsxModel.setEmail(email);
            xlsxModel.setNom(nom);
            xlsxModel.setPrenom(prenom);
            xlsxModel.setStatus(status);
            xlsxModel.setTarif(tarif);
            xlsxModel.setCodePostal(codePostal);
            xlsxModel.setEntrpriseProjet(entrpriseProjet);
            return xlsxModel;
        }
    }

    @Override
    public String toString() {
        return "XlsxModel{" +
                "date=" + date +
                ", email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", status='" + status + '\'' +
                ", tarif='" + tarif + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", entrpriseProjet='" + entrpriseProjet + '\'' +
                '}';
    }
}
