package model.enumeration;

public enum CardCurrency {
    EUR("EURO"),
    RON("RON");

    public final String label;
    CardCurrency(String label) {
        this.label = label;
    }
}
