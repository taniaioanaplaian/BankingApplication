package model.dto;

import javafx.scene.control.Button;

public class ClientCardDto {

    private String iban;
    private String cvv;
    private Double currentSum;
    private Double depositSum;
    Button deleteButton;

    public ClientCardDto(String iban, String cvv, Double currentSum, Double depositSum, Button deleteButton) {
        this.iban = iban;
        this.cvv = cvv;
        this.currentSum = currentSum;
        this.depositSum = depositSum;
        this.deleteButton = deleteButton;

    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Double getCurrentSum() {
        return currentSum;
    }

    public void setCurrentSum(Double currentSum) {
        this.currentSum = currentSum;
    }

    public Double getDepositSum() {
        return depositSum;
    }

    public void setDepositSum(Double depositSum) {
        this.depositSum = depositSum;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }


}
