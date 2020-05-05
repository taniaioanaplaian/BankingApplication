package model.entity;

public class Client {
    private Long clientId;
    private String firstName;
    private String lastName;
    private String ssn;

    public Client() {
    }

    public Client(Long clientId, String firstName, String lastName, String ssn) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
