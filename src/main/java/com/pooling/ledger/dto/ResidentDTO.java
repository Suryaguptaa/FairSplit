package com.pooling.ledger.dto;

public class ResidentDTO {

    private Long id;
    private String name;
    private String email;
    private String flatNumber;
    private Double balance;

    public ResidentDTO() {}

    public ResidentDTO(Long id, String name, String email, String flatNumber, Double balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.flatNumber = flatNumber;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}