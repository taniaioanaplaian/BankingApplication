package model.enumeration;

public enum AccountType {
    SHOPPING("SHOPPING_CARD"),
    SAVINGS("SAVINGS_CARD");

    public final String label;
    AccountType(String label) {
        this.label = label;
    }
}
