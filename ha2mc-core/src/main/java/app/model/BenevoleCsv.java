package app.model;

import java.util.ArrayList;
import java.util.List;

public class BenevoleCsv {

    private String date;
    private String nomPrenom;
    private String email;
    private String telephone;
    private String resolutions;
    private List<String> disponibilites = new ArrayList<>();
    private List<String> poles = new ArrayList<>();
    private List<String> actions = new ArrayList<>();
    private String autonomie;
    private String peurs;
    private String attentes;
    private String remarques;

    public BenevoleCsv() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getResolutions() {
        return resolutions;
    }

    public void setResolutions(String resolutions) {
        this.resolutions = resolutions;
    }

    public List<String> getDisponibilites() {
        return disponibilites;
    }

    public void setDisponibilites(List<String> disponibilites) {
        this.disponibilites = disponibilites;
    }

    public List<String> getPoles() {
        return poles;
    }

    public void setPoles(List<String> poles) {
        this.poles = poles;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public String getAutonomie() {
        return autonomie;
    }

    public void setAutonomie(String autonomie) {
        this.autonomie = autonomie;
    }

    public String getPeurs() {
        return peurs;
    }

    public void setPeurs(String peurs) {
        this.peurs = peurs;
    }

    public String getAttentes() {
        return attentes;
    }

    public void setAttentes(String attentes) {
        this.attentes = attentes;
    }

    public String getRemarques() {
        return remarques;
    }

    public void setRemarques(String remarques) {
        this.remarques = remarques;
    }

    @Override
    public String toString() {
        return "BenevoleCsv{" +
                "date='" + date + '\'' +
                ", nomPrenom='" + nomPrenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", resolutions='" + resolutions + '\'' +
                ", disponibilites=" + disponibilites +
                ", poles=" + poles +
                ", actions=" + actions +
                ", autonomie='" + autonomie + '\'' +
                ", peurs='" + peurs + '\'' +
                ", attentes='" + attentes + '\'' +
                ", remarques='" + remarques + '\'' +
                '}';
    }
}
