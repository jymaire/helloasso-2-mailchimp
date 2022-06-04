package model;

import java.time.LocalDateTime;

public class CsvMailChimpModel {

    private String formule;
    private String moyenDePaiement;
    private String nom;
    private String prenom;
    private String societe;
    private String date;
    private String email;
    private String codePostal;
    private String entrepriseProjet;

    public CsvMailChimpModel() {
        this.moyenDePaiement = "Carte bancaire";
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public String getMoyenDePaiement() {
        return moyenDePaiement;
    }

    public void setMoyenDePaiement(String moyenDePaiement) {
        this.moyenDePaiement = moyenDePaiement;
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

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
