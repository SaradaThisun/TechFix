package com.example.techtrack.models;

public class PaymentDetails {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;
    private String method;   // card, bank_transfer, pay_at_branch

    public PaymentDetails() {
    }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}