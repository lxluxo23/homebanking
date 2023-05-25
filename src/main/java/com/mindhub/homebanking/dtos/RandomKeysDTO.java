package com.mindhub.homebanking.dtos;

import java.util.List;

public class RandomKeysDTO {
    private double amount;
    private String description;
    private String fromAccountNumber;
    private String toAccountNumber;
    private List<String> randomKeys;

    public RandomKeysDTO() {
    }

    public RandomKeysDTO(double amount, String description, String fromAccountNumber, String toAccountNumber, List<String> randomKeys) {
        this.amount = amount;
        this.description = description;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.randomKeys = randomKeys;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public List<String> getRandomKeys() {
        return randomKeys;
    }

    public void setRandomKeys(List<String> randomKeys) {
        this.randomKeys = randomKeys;
    }
}
