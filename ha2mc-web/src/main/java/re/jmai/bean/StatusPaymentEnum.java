package re.jmai.bean;

public enum StatusPaymentEnum {
    todo("À faire"),
    tooLate("En retard"),
    success("Succès"),
    successAuto("Succès (automatique)"),
    fail("Echec"),
    waiting("En attente");

    private final String label;

    StatusPaymentEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
